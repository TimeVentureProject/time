package com.TimeVenture.model.dto.member;

import com.TimeVenture.model.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@Getter
public class ResponseMemberDto {

    private String name;
    private String email;
    private Timestamp regDate;
    private String img;
    private String auth;

    public ResponseMemberDto(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.regDate = member.getRegDate();
        this.img = member.getImg();
    }

    public ResponseMemberDto(Member member, String auth) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.regDate = member.getRegDate();
        this.img = member.getImg();
        this.auth = auth;
    }
}
