package com.TimeVenture.model.dto.member.google;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateMemberDto {

    private String name;
    private String img;
}
