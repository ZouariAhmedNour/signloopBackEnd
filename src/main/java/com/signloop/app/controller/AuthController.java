package com.signloop.app.controller;

import com.signloop.app.model.User;
import com.signloop.app.model.UserDto;
import com.signloop.app.security.JwtService;
import com.signloop.app.service.EmailService;
import com.signloop.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${app.url}")
    private String appUrl;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          EmailService emailService,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService
                          ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.emailService =  emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        User registered = userService.registerUser(user);
        String token = userService.createVerificationToken(registered);
        // Ici tu envoies le mail avec le lien contenant le token

        String verificationLink = appUrl + "/api/verify?token=" + token;

        emailService.sendEmail(
                user.getEmail(),
                "Vérification de votre compte",
                "Cliquez sur ce lien pour vérifier votre compte:\n" + verificationLink
        );

        return ResponseEntity.ok("User registered. Verification email sent.");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("token") String token) {
        if (userService.verifyUser(token)) {
            return ResponseEntity.ok("Email verified");
        }
        return ResponseEntity.badRequest().body("Invalid or expired token");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .status(403)
                    .body("Mauvais email ou mot de passe");
        }

        User user = userService.getByEmail(loginRequest.getEmail());

        if (!user.isEmailVerified()) {
            return ResponseEntity
                    .status(403)
                    .body("Veuillez vérifier votre email avant de vous connecter");
        }

        String token = jwtService.generateToken(user.getEmail());
//        return ResponseEntity.ok(Map.of(
//                "token", token,
//                "user", user
//        )
//        );
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setNom(user.getNom());
        userDto.setPrenom(user.getPrenom());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", userDto
        ));
    }


    @PostMapping("/reset-password-request")
    public ResponseEntity<String> requestReset(@RequestParam String email) {
        User user = userService.getByEmail(email);
        String token = userService.createPasswordResetToken(user);

        String resetLink = appUrl + "/reset-password?token=" + token;


        emailService.sendEmail(
                email,
                "Réinitialisation de votre mot de passe",
                "Cliquez sur ce lien pour réinitialiser votre mot de passe:\n" + resetLink
        );

        return ResponseEntity.ok("Email de réinitialisation envoyé.");
    }

    @PostMapping("/reset-password-confirm")
    public ResponseEntity<String> confirmReset(
            @RequestParam String token,
            @RequestParam String newPassword) {

        User user = userService.validatePasswordResetToken(token);

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);

        return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestParam String email) {
        User user = userService.getByEmail(email);
        if (user.isEmailVerified()) {
            return ResponseEntity.badRequest().body("Compte déjà vérifié.");
        }
        String token = userService.resendVerificationEmail(user);
        String verificationLink = appUrl + "/api/auth/verify?token=" + token;

        emailService.sendEmail(
                email,
                "Vérification de votre compte",
                "Cliquez sur ce lien pour vérifier votre compte:\n" + verificationLink
        );

        return ResponseEntity.ok("Email de vérification renvoyé.");
    }







}

