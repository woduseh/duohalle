package com.hac.duohalle.domain.index.controller;

import com.hac.duohalle.domain.account.entity.Account;
import com.hac.duohalle.infra.config.auth.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String index(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute("account", account);
        }
        return "index";
    }
}
