package com.hac.duohalle.domain.profile.controller;

import com.hac.duohalle.domain.profile.service.ProfileService;
import com.hac.duohalle.infra.config.auth.LoginAccount;
import com.hac.duohalle.infra.config.auth.SessionAccount;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/profile")
@Controller
public class ProfileController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ProfileService profileService;

    @GetMapping("/{email}")
    public String viewProfile(@LoginAccount SessionAccount account, @PathVariable String email,
            Model model) {
        logger.info("User try to view profile - Email: {}", email);

        model.addAttribute("account", profileService.findByEmail(email));

        if (account.getEmail().equals(email)) {
            model.addAttribute("isMyProfile", true);
        } else {
            model.addAttribute("isMyProfile", false);
        }

        return "profile/profile";
    }
}
