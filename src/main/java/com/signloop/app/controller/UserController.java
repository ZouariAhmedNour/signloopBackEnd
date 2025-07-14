package com.signloop.app.controller;

import com.signloop.app.model.User;
import com.signloop.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Récupère le profil de l'utilisateur connecté
     */
    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile() {
        String email = getAuthenticatedEmail();
        User user = userService.getByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Met à jour le profil de l'utilisateur connecté
     */
    @PutMapping("/me")
    public ResponseEntity<User> updateMyProfile(@RequestBody User newData) {
        String email = getAuthenticatedEmail();
        User user = userService.getByEmail(email);
        User updated = userService.updateProfile(user.getUserId(), newData);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprime le compte de l'utilisateur connecté
     */
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyAccount() {
        String email = getAuthenticatedEmail();
        User user = userService.getByEmail(email);
        userService.deleteUser(user.getUserId());
        return ResponseEntity.ok("Compte supprimé.");
    }

    private String getAuthenticatedEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
