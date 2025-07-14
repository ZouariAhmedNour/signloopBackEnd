package com.signloop.app.controller;

import com.signloop.app.model.User;
import com.signloop.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reset-password")
public class PasswordResetPageController {

    private final UserService userService;

    public PasswordResetPageController(UserService userService) {
        this.userService = userService;
    }

    // Affiche le formulaire avec le token
    @GetMapping
    public String showResetForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset_password";
    }

    // Traite la soumission du formulaire
    @PostMapping
    public String handleReset(
            @RequestParam String token,
            @RequestParam String newPassword,
            Model model
    ) {
        try {
            userService.resetPasswordByToken(token, newPassword);
            model.addAttribute("message", "Votre mot de passe a été changé avec succès.");
        } catch (RuntimeException e) {
            model.addAttribute("message", "Erreur : " + e.getMessage());
        }
        return "reset_password";
    }
}
