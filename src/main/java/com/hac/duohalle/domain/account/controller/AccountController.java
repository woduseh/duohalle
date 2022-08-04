package com.hac.duohalle.domain.account.controller;

import com.hac.duohalle.domain.account.dto.request.AccountConfirmRequestDto;
import com.hac.duohalle.domain.account.dto.request.AccountSignUpRequestDto;
import com.hac.duohalle.domain.account.entity.Account;
import com.hac.duohalle.domain.account.service.AccountService;
import com.hac.duohalle.domain.account.validator.AccountSignUpRequestDtoValidator;
import com.hac.duohalle.infra.config.auth.CurrentUser;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AccountService accountService;
    private final AccountSignUpRequestDtoValidator signUpRequestDtoValidator;

    @InitBinder("accountSignUpRequestDto")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(signUpRequestDtoValidator);
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute(new AccountSignUpRequestDto());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Valid AccountSignUpRequestDto accountSignUpRequestDto, Errors error) {
        if (error.hasErrors()) {
            logger.warn("sign-up failure - form: {}, Error: {}", accountSignUpRequestDto,
                    error.getAllErrors());
            return "account/sign-up";
        }

        Account account = accountService.signUp(accountSignUpRequestDto);
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String confirm(AccountConfirmRequestDto dto, Model model) {
        logger.info("User try to confirm account - Email: {}", dto.getEmail());
        Account account = accountService.confirm(dto);

        accountService.login(account);
        model.addAttribute("isEmailVerified", account.isEmailVerified());
        return "account/confirm";
    }

    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }

    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentUser Account account, Model model) {
        if (!account.canSendConfirmEmail()) {
            model.addAttribute("error", "인증 메일은 1시간에 한번만 전송할 수 있습니다.");
            model.addAttribute("isEmailVerified", account.isEmailVerified());
        } else {
            accountService.resendConfirmEmail(account.getEmail());
            model.addAttribute("isEmailVerified", account.isEmailVerified());
        }
        return "account/confirm";
    }
}
