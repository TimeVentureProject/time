package com.TimeVenture.model.dto.projectMember;

import com.TimeVenture.model.entity.projectMember.ProjectMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MainResponseProjectMemberDto {
    private int projectMemberId;
    private int projectId;
    private String memberName;
    private String memberEmail;
    private String auth;

    public static MainResponseProjectMemberDto fromEntity(ProjectMember projectMember) {
        return new MainResponseProjectMemberDto(
                projectMember.getProjectMemberId(),
                projectMember.getProject().getPid(),
                projectMember.getMember().getName(),
                projectMember.getMember().getEmail(),
                projectMember.getAuth().toString()
        );
    }
}
