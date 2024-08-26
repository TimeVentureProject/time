package com.TimeVenture.model.dto.project;

import com.TimeVenture.model.entity.project.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddProjectRequestDto {


    private String pExplain;

    private String pName;

    private Timestamp pStartDate;

    private Timestamp pCreateDate;

    private Timestamp pUpdateDate;

    private Timestamp pEndDate;

    private String pColor;

    public Project toEntity() {
        return Project.builder()
                .pExplain(pExplain)
                .pName(pName)
                .pStartDate(pStartDate)
                .pEndDate(pEndDate)
                .pColor(pColor)
                .build();
    }


}
