package com.example.spring_etrees.adapter.web;

import com.example.spring_etrees.application.member.provided.MemberRegister;
import com.example.spring_etrees.domain.member.MemberCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRegister memberRegister;

    /**
     * 회원가입 폼 페이지
     */
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("memberCreateRequest", new MemberCreateRequest(
                "", "", "", "", "", "", "", ""
        ));
        return "member/join";
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute MemberCreateRequest request,
                       BindingResult bindingResult,
                       Model model) {

        // 1. 유효성 검증 실패 시
        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        try {
            // 2. 회원가입 처리
            Long memberId = memberRegister.registerMember(request);

            // 3. 성공 시 로그인 페이지로 리다이렉트
            return "redirect:/login?joined=true";

        } catch (IllegalArgumentException e) {
            // 4. 비즈니스 로직 에러 (중복 아이디, 비밀번호 불일치 등)
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }
    }
}
