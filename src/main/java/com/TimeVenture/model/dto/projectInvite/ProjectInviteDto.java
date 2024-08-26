package com.TimeVenture.model.dto.projectInvite;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import lombok.Data;

@Data
public class ProjectInviteDto {
    private String email;
    private String managerEmail;
    private String projectId;
    private String projectName;
}
