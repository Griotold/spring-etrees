package com.example.spring_etrees.adapter.web;

import com.example.spring_etrees.adapter.security.LoginUser;
import com.example.spring_etrees.application.member.provided.MemberFinder;
import com.example.spring_etrees.application.member.provided.MemberRegister;
import com.example.spring_etrees.domain.member.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /**
     * 회원 목록 조회 (관리자만)
     */
    @GetMapping("/admin/members")
    @PreAuthorize("hasRole('ADMIN')")
    public String memberList(@RequestParam(defaultValue = "1") int pageNo,
                             @RequestParam(defaultValue = "10") int size,
                             @AuthenticationPrincipal LoginUser loginUser,
                             Model model) {

        // 관리자 권한 체크
        if (!loginUser.getMember().isAdmin()) {
            throw new AccessDeniedException("관리자만 접근 가능합니다.");
        }

        Pageable pageable = PageRequest.of(pageNo - 1, size);
        Page<Member> memberPage = memberFinder.findAll(pageable);

        model.addAttribute("memberPage", memberPage);
        model.addAttribute("currentPage", pageNo);

        return "admin/member-list";
    }

    /**
     * 일반회원을 관리자로 승격
     */
    @PostMapping("/admin/members/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public String promoteToAdmin(@RequestParam("memberId") Long memberId,
                                 RedirectAttributes redirectAttributes) {
        try {
            Member promotedMember = memberRegister.promoteToAdmin(memberId);
            redirectAttributes.addFlashAttribute("message",
                    promotedMember.getName() + "님을 관리자로 승격했습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/members";
    }

    /**
     * 관리자를 일반회원으로 강등
     */
    @PostMapping("/admin/members/demote")
    @PreAuthorize("hasRole('ADMIN')")
    public String demoteToUser(@RequestParam("memberId") Long memberId,
                               RedirectAttributes redirectAttributes) {
        try {
            Member demotedMember = memberRegister.demoteToUser(memberId);
            redirectAttributes.addFlashAttribute("message",
                    demotedMember.getName() + "님을 일반회원으로 강등했습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/members";
    }
}
