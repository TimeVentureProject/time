package com.TimeVenture.model.dto.member.google;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.model.enums.Auth;
import com.TimeVenture.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Getter
public class SessionDTO {
    private String email;
    private String name;
    private String img;
    private Role role;
    private Timestamp regDate;
    private Auth auth;

    public SessionDTO(Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.img = member.getImg();
        this.role = member.getRole();
        this.regDate = member.getRegDate();
    }

    public SessionDTO(Member member, ProjectMember projectMember) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.img = member.getImg();
        this.role = member.getRole();
        this.regDate = member.getRegDate();
        this.auth = projectMember.getAuth(); // loginAccountRole을 지나면 auth 부분 바꾸기
    }
}
