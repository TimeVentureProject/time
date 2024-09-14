package com.TimeVenture.config;

import com.TimeVenture.config.member.auth.CustomAuthenticationSuccessHandler;
import com.TimeVenture.config.member.auth.CustomOAuth2UserService;
import com.TimeVenture.config.member.auth.EmailAuthenticationSuccessHandler;
import com.TimeVenture.model.enums.Role;
import com.TimeVenture.service.member.email.impl.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final EmailAuthenticationSuccessHandler emailAuthenticationSuccessHandler;

    //사용자 서비스 주입
    private final MemberDetailsService memberDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            //csrf 비활성화
            .csrf(csrf -> csrf.disable())
            //X-Frame-Options 비활성화 (html frame, iframe태그의 렌더링 금지를 비활성화)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .authorizeHttpRequests(
                        // 모든 계정이 접속 가능하도록 설정
                        request -> request
                                .requestMatchers("/css/**", "/image/**", "/js/**").permitAll()
                                .requestMatchers("/", "/main", "/join", "/confirmEmail", "/verify"
                                        , "/joinFirstProj", "/joinAccountRole", "/joinNewTask", "/sendInvitationEmail"
                                        , "/joinFinish", "/joinCheck", "/inviteJoinFinish", "/joinGoogleAccountSetup"
                                        ,"/oauth2/authorization/google", "/login/oauth2/**", "/updateAccount", "/login/oauth2/code/google").permitAll()
                                // admin 접근 권한 설정
                                .requestMatchers("/admin/**").hasAnyAuthority(Role.ADMIN.name())
                                // user 접근 권한 설정
                                .requestMatchers("/user/**").hasAnyAuthority(Role.USER.name())
                                // 그 외 모든 요청은 인증이 필요함
                                .anyRequest().authenticated())
            .formLogin(form -> form
                    .loginPage("/login")
                    .successHandler(emailAuthenticationSuccessHandler)
                    .failureUrl("/join")
                    .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login"))

            .oauth2Login(oauth -> oauth
                    .loginPage("/login")
                    .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))
                    .successHandler(customAuthenticationSuccessHandler)
            );
            return http.build();
    }

    // 회원가입 시 비밀번호를 암호화하기 위한 BCryptPasswordEncoder 빈 생성
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}