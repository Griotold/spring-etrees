package com.example.spring_etrees.adapter.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "joined", required = false) String joined,
                            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        if (joined != null) {
            model.addAttribute("successMessage", "회원가입이 완료되었습니다. 로그인해주세요.");
        }

        return "member/login";
    }
}
