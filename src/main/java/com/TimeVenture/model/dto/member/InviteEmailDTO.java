package com.TimeVenture.model.dto.member;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import lombok.Data;

@Data
public class InviteEmailDTO {

    private String email1;
    private String email2;
    private String email3;
    private String email4;
    private Member managerEmail;
    private Project projectId;
    private String projectName;
}
