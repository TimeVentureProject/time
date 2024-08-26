package com.TimeVenture.model.dto.myPage;

import com.TimeVenture.model.entity.member.Member;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UpdateMyPageDto {

    private String name;
    private String introduction;

    public UpdateMyPageDto(Member member) {
        this.name = member.getName();
        this.introduction = member.getIntroduction();
    }
}
