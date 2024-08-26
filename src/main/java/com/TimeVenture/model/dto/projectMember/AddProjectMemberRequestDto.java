package com.TimeVenture.model.dto.projectMember;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.model.enums.Auth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //JSON 데이터를 자바 객체로 직렬화하기 위해서 기본 생성자가 필요
@AllArgsConstructor //모든 인자를 가진 생성자를 통해
@Getter
public class AddProjectMemberRequestDto {

    private int projectId;
    private String memberId;
    private Auth auth;

    public ProjectMember toEntity(Project projectId, Member memberId) { // 변환하는 과정이기 때문에 객체 자체를 사용해야 한다.
        return ProjectMember.builder()
                .project(projectId)
                .member(memberId)
                .auth(auth)
                .build();
    }
}
