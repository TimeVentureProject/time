package com.TimeVenture.controller.member.email.controller;//package com.TimeVenture.join.controller;

import com.TimeVenture.model.dto.member.email.JoinRequestDto;
import com.TimeVenture.repository.member.MemberRepository;
import com.TimeVenture.service.member.email.JoinService;
import com.TimeVenture.service.member.email.exception.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class invitationController {

    private final MemberRepository memberRepository;

    private final JoinService joinService;

    //회원가입 접근
    @GetMapping("/joinCheck")
    public String join(@RequestParam("email") String email
                        ,@RequestParam("projectId") String projectId
                        ,Model model) {
        // 초대받은 회원의 이메일을 조회해서 있을 경우
        if(memberRepository.existsByEmail(email)) {
            // 회원의 기존 정보로 프로젝트 멤버 DB 생성
            joinService.existMemberUpdate(email,
                    Integer.parseInt(projectId));
            // 로그인 페이지로 리다이렉트
            System.out.println("유저가 존재합니다.");
            return "redirect:/login";
        }
        // 회원이 아닐 경우 email과 projectId를 회원가입 페이지로 전달
        System.out.println("유저가 존재하지않습니다.");
        model.addAttribute("email", email);
        model.addAttribute("projectId", projectId);
        return "member/invitationAccountSetup";
    }

    //회원가입 접근
    @PostMapping("/inviteJoinFinish")
    public String joinProcess(@RequestParam("projectId") String projectId,
                                JoinRequestDto joinRequestDto) throws DuplicateEmailException {
        joinService.join(joinRequestDto);
        joinService.existMemberUpdate(joinRequestDto.getEmail(), Integer.parseInt(projectId));
        return "member/joinFinish";
    }


}
