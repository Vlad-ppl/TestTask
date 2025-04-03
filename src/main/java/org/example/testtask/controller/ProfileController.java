package org.example.testtask.controller;

import lombok.RequiredArgsConstructor;
import org.example.testtask.dto.User;
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
    public String home(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
                       Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        User user = profileService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        User user = profileService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                @AuthenticationPrincipal UserDetails userDetails) {
        profileService.updateUserProfile(userDetails.getUsername(), updatedUser);
        return "redirect:/";
    }
    @GetMapping("/profile")
    public String redirectToRoot() {
        return "redirect:/";
    }

}
