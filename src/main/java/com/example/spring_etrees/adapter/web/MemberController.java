package com.example.spring_etrees.adapter.web;

import com.example.spring_etrees.adapter.security.LoginUser;
import com.example.spring_etrees.application.member.provided.MemberFinder;
import com.example.spring_etrees.application.member.provided.MemberRegister;
import com.example.spring_etrees.domain.member.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final MemberFinder memberFinder;

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
            Member member = memberRegister.registerMember(request);

            // 3. 성공 시 로그인 페이지로 리다이렉트
            return "redirect:/login?joined=true";

        } catch (IllegalArgumentException | DuplicateUsernameException | DuplicateNameException | PasswordMismatchException e) {
            // 4. 비즈니스 로직 에러 (중복 아이디, 중복 이름, 비밀번호 불일치 등)
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }
    }

    /**
     * 내 프로필 보기
     */
    @GetMapping("/member/profile")
    public String profile(@AuthenticationPrincipal LoginUser loginUser, Model model) {
        Member member = memberFinder.findById(loginUser.getMember().getId());
        model.addAttribute("member", member);
        return "member/profile";
    }

    /**
     * 내 정보 수정 폼
     */
    @GetMapping("/member/edit")
    public String editForm(@AuthenticationPrincipal LoginUser loginUser, Model model) {
        Member member = memberFinder.findById(loginUser.getMember().getId());

        // 기존 정보로 폼 데이터 생성
        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest(
                member.getName(),
                member.getPhone(),
                member.getPostNo(),
                member.getAddress(),
                member.getCompany()
        );

        model.addAttribute("memberInfoUpdateRequest", updateRequest);
        model.addAttribute("member", member); // 읽기전용 정보용
        return "member/edit";
    }

    /**
     * 내 정보 수정 처리
     */
    @PostMapping("/member/edit")
    public String edit(@AuthenticationPrincipal LoginUser loginUser,
                       @Valid @ModelAttribute MemberInfoUpdateRequest request,
                       BindingResult bindingResult,
                       Model model) {

        if (bindingResult.hasErrors()) {
            Member member = memberFinder.findById(loginUser.getMember().getId());
            model.addAttribute("member", member);
            return "member/edit";
        }

        try {
            memberRegister.updateInfoMember(request, loginUser.getMember().getId());
            return "redirect:/member/profile?updated=true";
        } catch (IllegalArgumentException | DuplicateNameException e) {
            Member member = memberFinder.findById(loginUser.getMember().getId());
            model.addAttribute("member", member);
            model.addAttribute("errorMessage", e.getMessage());
            return "member/edit";
        }
    }
}
