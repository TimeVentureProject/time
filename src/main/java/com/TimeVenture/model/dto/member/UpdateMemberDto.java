package com.TimeVenture.model.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateMemberDto {

    private String name;
    private String img;
    private Timestamp regDate;
}
