package com.TimeVenture.repository.projectMember;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {
    List<ProjectMember> findAllByProject(Project project); //특정 프로젝트에서 해당 멤버 구성원 찾기

    Optional<ProjectMember> findByProjectAndMember(Project project, Member member);

    List<ProjectMember> findByMember_Email(String email);

}
