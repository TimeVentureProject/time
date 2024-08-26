package com.TimeVenture.model.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateProjectDto {
    private String pExplain;
    private String pName;
    private Timestamp pStartDate;
    private Timestamp pEndDate;
    private String pColor;
}
