package com.TimeVenture.model.dto.member.google;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@Getter
public class ResponseMemberDto {

    private String email;
    private String name;
    private String pwd;
    private String img;
    private Timestamp regDate;
    private String loginType;
    private String refreshToken;
    private Role role;

    public ResponseMemberDto(Member member) {
        this.email=member.getEmail();
        this.name = member.getName();
        this.pwd=member.getPwd();
        this.img=member.getImg();
        this.regDate = member.getRegDate();
        this.loginType=member.getLoginType();
        this.refreshToken=member.getRefreshToken();
        this.role=member.getRole();
    }
}
