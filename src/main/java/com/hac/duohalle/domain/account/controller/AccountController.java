package com.hac.duohalle.domain.account.controller;

import com.hac.duohalle.domain.account.dto.request.AccountSignUpRequestDto;
import com.hac.duohalle.domain.account.service.AccountService;
import javax.mail.MessagingException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sign-up")
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AccountService accountService;

    @GetMapping
    public String signUp(Model model) {
        model.addAttribute(new AccountSignUpRequestDto());
        return "account/sign-up";
    }

    @GetMapping("/confirm/{email}")
    public String confirm(@PathVariable String email) {
        logger.info("User {} try to confirm account", email);
        accountService.confirm(email);
        return "redirect:/";
    }

    @PostMapping
    public String signUp(@Valid AccountSignUpRequestDto form, Errors error)
            throws MessagingException {
        if (error.hasErrors()) {
            logger.info("sign-up failure - form: {}, error: {}", form, error.getAllErrors());
            return "account/sign-up";
        }

        accountService.signUp(form);
        return "redirect:/sign-in";
    }
}
