package com.signloop.app.service;

import com.signloop.app.model.PasswordResetToken;
import com.signloop.app.model.User;
import com.signloop.app.model.VerificationToken;
import com.signloop.app.repository.PasswordResetTokenRepository;
import com.signloop.app.repository.UserRepository;
import com.signloop.app.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public UserService(UserRepository userRepository,
                       VerificationTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder,
                       PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    /**
     * Enregistre un utilisateur avec mot de passe chiffré et email non vérifié
     */
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec cet email: " + email));
    }
    @Transactional
    public String createPasswordResetToken(User user) {

        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(2)); // valable 2h
        passwordResetTokenRepository.save(resetToken);
        return token;
    }

    /**
     * Valide le token de reset password et retourne l'utilisateur
     */
    public User validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> optional = passwordResetTokenRepository.findByToken(token);
        if (optional.isEmpty()) {
            throw new RuntimeException("Token invalide");
        }
        PasswordResetToken prt = optional.get();
        if (prt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expiré");
        }
        // tu peux supprimer le token après usage
        passwordResetTokenRepository.delete(prt);
        return prt.getUser();
    }

    /**
     * Sauvegarde un utilisateur
     */
    public User save(User user) {
        return userRepository.save(user);
    }



    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà.");
        }
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword(user.getPassword());
        user.setRole("USER");
        user.setEmailVerified(false);
        return userRepository.save(user);
    }

    /**
     * Crée un token de vérification pour email
     */
    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        tokenRepository.save(verificationToken);
        return token;
    }

    /**
     * Vérifie un utilisateur via son token
     */
    public boolean verifyUser(String token) {
        Optional<VerificationToken> optional = tokenRepository.findByToken(token);
        if (optional.isEmpty()) {
            return false;
        }

        VerificationToken vt = optional.get();
        if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false; // Expiré
        }

        User user = vt.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
        tokenRepository.delete(vt);
        return true;
    }

    /**
     * Réinitialise le mot de passe via email
     */
    public boolean resetPassword(String email, String newPassword) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            return false;
        }
        User user = optional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    /**
     * Met à jour les données du profil utilisateur
     */
    public User updateProfile(Long userId, User newData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (newData.getNom() != null) user.setNom(newData.getNom());
        if (newData.getPrenom() != null) user.setPrenom(newData.getPrenom());
        if (newData.getTelephone() != null) user.setTelephone(newData.getTelephone());
        if (newData.getAdresse() != null) user.setAdresse(newData.getAdresse());

        return userRepository.save(user);
    }

    /**
     * Supprime le compte utilisateur
     */
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        userRepository.deleteById(userId);
    }

    public String resendVerificationEmail(User user) {
        // Supprimer le token existant s'il existe
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

        // Créer un nouveau token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        tokenRepository.save(verificationToken);

        return token;
    }

    public void resetPasswordByToken(String token, String newPassword) {
        User user = validatePasswordResetToken(token);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.deleteByToken(token);
    }





}
