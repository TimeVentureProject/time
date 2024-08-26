package com.TimeVenture.model.dto.member.google;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Getter
public class AddMemberRequestDto {
    private String email;
    private String name;
    private String pwd;
    private String img;
    private Timestamp regDate;
    private String loginType;
    private String refreshToken;
    private Role role;

    public Member toEntity() {
        // role이 null이면 기본 역할을 USER로 설정
        Role assignedRole = (role != null) ? role : Role.USER;

        // regDate가 null이면 현재 시간으로 설정
        Timestamp registrationDate = (regDate != null) ? regDate : new Timestamp(System.currentTimeMillis());

        return Member.builder()
                .email(email)
                .name(name)
                .pwd(pwd)
                .img(img)
                .loginType(loginType)
                .role(assignedRole)
                .regDate(registrationDate)
                .build();
    }
}
