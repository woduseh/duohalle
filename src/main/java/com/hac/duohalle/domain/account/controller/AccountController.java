package com.hac.duohalle.domain.account.controller;

import com.hac.duohalle.domain.account.dto.request.AccountSignUpRequestDto;
import com.hac.duohalle.domain.account.service.AccountService;
import com.hac.duohalle.domain.account.validator.AccountSignUpRequestDtoValidator;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sign-up")
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AccountService accountService;

    private final AccountSignUpRequestDtoValidator signUpRequestDtoValidator;

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

    @InitBinder("signUpRequestDto")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(signUpRequestDtoValidator);
    }

    @PostMapping
    public String signUp(@Valid AccountSignUpRequestDto signUpRequestDto, Errors error) {
        if (error.hasErrors()) {
            logger.info("sign-up failure - form: {}, error: {}", signUpRequestDto,
                    error.getAllErrors());
            return "account/sign-up";
        }

        accountService.signUp(signUpRequestDto);
        return "redirect:/sign-in";
    }
}
