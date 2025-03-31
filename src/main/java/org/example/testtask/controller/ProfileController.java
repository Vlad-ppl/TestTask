package org.example.testtask.controller;

import lombok.RequiredArgsConstructor;
import org.example.testtask.entity.UserEntity;
import org.example.testtask.security.CustomUserDetails;
import org.example.testtask.service.ProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails,
                       Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        UserEntity user = profileService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        UserEntity user = profileService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute("user") UserEntity updatedUser,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserEntity user = profileService.getUserByEmail(userDetails.getUsername());
        profileService.updateUserProfile(user, updatedUser);
        return "redirect:/";
    }
}
