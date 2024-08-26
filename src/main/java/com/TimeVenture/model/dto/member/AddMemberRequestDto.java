package com.TimeVenture.model.dto.member;

import com.TimeVenture.model.entity.member.Member;
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
    private String img;
    private Timestamp regDate;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .name(name)
                .img(img)
                .build();
    }
}
