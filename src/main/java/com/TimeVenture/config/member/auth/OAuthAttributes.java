package com.TimeVenture.config.member.auth;

import com.TimeVenture.model.dto.member.google.AddMemberRequestDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.enums.Role;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String img;
    private Role role;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.img = img;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        // AddMemberRequestDto를 사용하여 Member 엔티티 생성
        AddMemberRequestDto dto = new AddMemberRequestDto();
        dto.setEmail(email);
        dto.setName(name);
        dto.setPwd(""); // 임시 비밀번호 설정
        dto.setImg(img);
        dto.setLoginType("google");
        dto.setRegDate(new Timestamp(System.currentTimeMillis()));
        dto.setRole(Role.USER); // 기본 역할은 DTO에서 설정됨

        return dto.toEntity();
    }
}
