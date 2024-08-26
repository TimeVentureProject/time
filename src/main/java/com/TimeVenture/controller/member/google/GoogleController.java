package com.TimeVenture.controller.member.google;

import com.TimeVenture.model.dto.member.google.SessionDTO;
import com.TimeVenture.repository.member.MemberRepository;
import com.TimeVenture.service.member.google.GoogleMemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class GoogleController {

    private final MemberRepository memberRepository;
    private final GoogleMemberService googleMemberService;

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    //구글 계정 로그인 이후
    @GetMapping("/joinAccount")
    public String joinAccount(HttpSession session, Model model) {
        SessionDTO sessionDTO = (SessionDTO) session.getAttribute("user");

        model.addAttribute("email", sessionDTO.getEmail());
        model.addAttribute("name", sessionDTO.getName());
        return "member/joinAccount";
    }
    
    //
    @GetMapping("/joinGoogleAccountSetup")
    public String joinGoogleAccountSetup(@RequestParam("email") String email,
                                         @RequestParam("name") String name, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        return "member/joinGoogleAccountSetup";
    }
    
    //디비에 회원가입 정보가 최종 저장
    @RequestMapping("/updateAccount")
    public String updateAccount(@RequestParam("email") String email,
                                @RequestParam("name") String name,
                                @RequestParam("profilePicture") MultipartFile profilePicture,
                                Model model) throws IOException {

        System.out.println("updateAccount: " +email);

        googleMemberService.updateAccount(email, name, profilePicture);
        model.addAttribute("email", email);
        return "member/joinAccountRole";
    }

}
