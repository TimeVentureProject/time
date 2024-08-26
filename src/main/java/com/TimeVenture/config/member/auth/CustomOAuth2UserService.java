package com.TimeVenture.config.member.auth;

import com.TimeVenture.model.dto.member.google.AddMemberRequestDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.enums.Role;
import com.TimeVenture.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        // Safely extract refresh token if available
        String refreshToken = null;
        Map<String, Object> additionalParameters = userRequest.getAdditionalParameters();
        if (additionalParameters.containsKey("refresh_token")) {
            refreshToken = additionalParameters.get("refresh_token").toString();
        }

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes, refreshToken);
        System.out.println(member.getRole());
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + member.getRole())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private Member saveOrUpdate(OAuthAttributes attributes, String refreshToken) {
        return memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> {
                    entity.update(attributes.getName(), attributes.getImg());
                    return entity;
                })
                .orElseGet(() -> {
                    AddMemberRequestDto dto = new AddMemberRequestDto();
                    dto.setEmail(attributes.getEmail());
                    dto.setName(attributes.getName());
                    dto.setPwd(""); // 임시 비밀번호 설정
                    dto.setImg(attributes.getImg());
                    dto.setLoginType("google");
                    dto.setRegDate(new Timestamp(System.currentTimeMillis()));
                    dto.setRole(Role.USER); // 기본 역할은 DTO에서 설정됨

                    Member newMember = dto.toEntity();
                    newMember.updateRefreshToken(refreshToken);
                    return memberRepository.save(newMember);
                });
    }
}
