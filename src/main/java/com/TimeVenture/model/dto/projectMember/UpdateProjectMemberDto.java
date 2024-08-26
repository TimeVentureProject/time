package com.TimeVenture.model.dto.projectMember;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.enums.Auth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateProjectMemberDto {

    private int projectId;
    private String memberId;
    private String auth;

    public UpdateProjectMemberDto(Project project, Member member) {
        this.projectId = project.getPid();
        this.memberId = member.getEmail();
        this.auth = getAuth();
    }
}
