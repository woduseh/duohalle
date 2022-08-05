package com.hac.duohalle.domain.index.controller;

import com.hac.duohalle.infra.config.auth.LoginAccount;
import com.hac.duohalle.infra.config.auth.SessionAccount;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(@LoginAccount SessionAccount account, Model model) {
        if (account != null) {
            model.addAttribute("account", account);
        } else {
            model.addAttribute("error", List.of("서비스를 이용하려면 로그인해야 합니다!"));
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
