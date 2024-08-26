package com.TimeVenture.model.entity.member;

import com.TimeVenture.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Member")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "email")
public class Member implements UserDetails {

    @Id
    @Column(name = "m_id")
    @NotNull
    private String email; // 이메일을 ID로 사용

    @Column(name = "m_name")
    @NotNull
    private String name;

    @Column(name = "m_pwd")
    private String pwd;

    @Column(name = "m_img")
    private String img;

    @Column(name = "m_regdate")
    @NotNull
    private Timestamp regDate;

    @Column(name = "m_logintype")
    @NotNull
    private String loginType;

    @Column(name = "m_token")
    private String refreshToken;

    @Column(name = "m_introduction")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "m_role")
    private Role role;

    @Builder
    public Member(String email, String name, String pwd, String img, String loginType, Timestamp regDate, Role role, String introduction) {
        this.email = email;
        this.name = name;
        this.pwd = pwd;
        this.img = img;
        this.regDate=regDate;
        this.loginType = loginType;
        this.role = role != null ? role : Role.USER; // 기본 역할을 USER로 설정
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    //사용할 아이디
    @Override
    public String getUsername() {
        return email;
    }

    //계정의 유효성
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정의 락 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public Member update(String name, String img) {
        this.name = name;
        this.img = img;
        return this;
    }

    // 업데이트 메서드 추가
    public Member updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

}
