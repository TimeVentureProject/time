package com.TimeVenture.config.member.auth;

import com.TimeVenture.model.dto.member.google.SessionDTO;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.repository.member.MemberRepository;
import com.TimeVenture.repository.projectMember.ProjectMemberRepository;
import com.TimeVenture.service.member.email.impl.MemberDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        MemberDetails memberDetails = (MemberDetails)authentication.getPrincipal();
        Optional<Member> member = memberRepository.findByEmail(memberDetails.getUsername());
        SessionDTO sessionDTO = new SessionDTO(member.get());

        HttpSession session = request.getSession();
        session.setAttribute("member", sessionDTO);

        // 성공 후 리디렉션
        response.sendRedirect("/main");
    }
}
