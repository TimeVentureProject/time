package com.TimeVenture.config.member.auth;

import com.TimeVenture.model.dto.member.google.SessionDTO;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.repository.member.MemberRepository;
import com.TimeVenture.repository.projectMember.ProjectMemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        System.out.println("Authentication email: " + email);

        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            System.out.println("User not found for email: " + email);
            throw new IllegalStateException("User not found");
        }
        System.out.println("Found member: " + member);

        Timestamp regDate = member.getRegDate();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        long secondsSinceReg = ChronoUnit.SECONDS.between(regDate.toLocalDateTime(), now.toLocalDateTime());

        HttpSession session = request.getSession();
        if (secondsSinceReg <= 30) {  // 30초 이내인지 확인
            SessionDTO sessionDTO = new SessionDTO(member);
            session.setAttribute("user", sessionDTO);
            response.sendRedirect("/joinAccount");
        }

            SessionDTO sessionDTO = new SessionDTO(member);
            session.setAttribute("member", sessionDTO);
            response.sendRedirect("/main");
        }
    }

