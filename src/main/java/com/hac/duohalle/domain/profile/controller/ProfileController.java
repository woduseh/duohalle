package com.hac.duohalle.domain.profile.controller;

import com.hac.duohalle.domain.profile.dto.request.NicknameChangeRequestDto;
import com.hac.duohalle.domain.profile.dto.request.PasswordChangeRequestDto;
import com.hac.duohalle.domain.profile.dto.request.ProfileChangeRequestDto;
import com.hac.duohalle.domain.profile.dto.response.ProfileResponseDto;
import com.hac.duohalle.domain.profile.service.ProfileService;
import com.hac.duohalle.domain.profile.validator.NicknameValidator;
import com.hac.duohalle.domain.profile.validator.PasswordFormValidator;
import com.hac.duohalle.infra.config.auth.LoginAccount;
import com.hac.duohalle.infra.config.auth.SessionAccount;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@RequestMapping("/profile")
@Controller
public class ProfileController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ProfileService profileService;

    private final NicknameValidator nicknameValidator;

    private final PasswordFormValidator passwordFormValidator;

    private final ModelMapper modelMapper;


    @InitBinder("passwordChangeRequestDto")
    public void passwordFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(passwordFormValidator);
    }

    @InitBinder("nicknameChangeRequestDto")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameValidator);
    }

    @GetMapping("/{nickname}")
    public String viewProfile(@LoginAccount SessionAccount account, @PathVariable String nickname,
            Model model) {
        model.addAttribute("profile", profileService.findByNickname(nickname));

        if (account.getNickname().equals(nickname)) {
            model.addAttribute("isMyProfile", true);
        } else {
            model.addAttribute("isMyProfile", false);
        }

        return "account/profile";
    }

    @GetMapping("/settings/profile")
    public String updateProfileView(@LoginAccount SessionAccount account, Model model) {
        ProfileResponseDto profile = profileService.findByNickname(account.getNickname());

        ProfileChangeRequestDto profileChangeRequestDto = modelMapper.map(profile,
                ProfileChangeRequestDto.class);
        model.addAttribute(profileChangeRequestDto);
        model.addAttribute("profile", profile);

        return "profile/profile";
    }

    @PostMapping("/settings/profile")
    public String updateProfile(@LoginAccount SessionAccount account,
            @Valid ProfileChangeRequestDto profileChangeRequestDto, RedirectAttributes attributes) {
        logger.info("User try to update profile - Email: {}", account.getEmail());

        profileService.updateProfile(account, profileChangeRequestDto);

        attributes.addFlashAttribute("info", "프로필을 변경했습니다.");
        return "redirect:/profile/" + account.getNickname();
    }

    @GetMapping("/settings/password")
    public String updatePasswordView(@LoginAccount SessionAccount account, Model model) {
        model.addAttribute(new PasswordChangeRequestDto());
        model.addAttribute("profile", profileService.findByNickname(account.getNickname()));

        return "profile/password";
    }

    @PostMapping("/settings/password")
    public String updatePassword(@LoginAccount SessionAccount account,
            @Valid PasswordChangeRequestDto passwordChangeRequestDto,
            RedirectAttributes attributes) {
        logger.info("User try to update password - Email: {}", account.getEmail());

        profileService.updatePassword(account, passwordChangeRequestDto.getNewPassword());

        attributes.addFlashAttribute("info", "패스워드를 변경했습니다.");
        return "redirect:/profile/" + account.getNickname();
    }

    @GetMapping("/settings/account")
    public String updateAccountView(@LoginAccount SessionAccount account, Model model) {
        model.addAttribute(new NicknameChangeRequestDto());
        model.addAttribute("profile", profileService.findByNickname(account.getNickname()));

        return "profile/account";
    }

    @PostMapping("/settings/account")
    public String updateAccount(@LoginAccount SessionAccount account,
            @Valid NicknameChangeRequestDto nicknameChangeRequestDto,
            RedirectAttributes attributes) {
        logger.info("User try to update account - Email: {}", account.getEmail());

        profileService.updateNickname(account, nicknameChangeRequestDto.getNickname());

        attributes.addFlashAttribute("info", "닉네임을 변경했습니다.");
        return "redirect:/profile/" + account.getNickname();
    }

}
