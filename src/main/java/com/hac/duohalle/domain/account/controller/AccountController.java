package com.hac.duohalle.domain.account.controller;

import com.hac.duohalle.domain.account.form.SignUpForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }
}
