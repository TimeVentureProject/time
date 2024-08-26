package com.TimeVenture.model.dto.projectMember;

import com.TimeVenture.model.entity.projectMember.ProjectMember;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseProjectMemberDto {

    private int projectMemberId; //클라이언트에게 반환해야하는 pk키
    private int projectId;
    private String memberId;
    private String auth;

    public ResponseProjectMemberDto(ProjectMember projectMember) {
        this.projectMemberId = projectMember.getProjectMemberId();
        this.projectId = projectMember.getProject().getPid();
        this.memberId = projectMember.getMember().getEmail();
        this.auth = projectMember.getAuth().toString();
    }
}
