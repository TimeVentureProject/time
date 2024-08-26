package com.TimeVenture.repository.project;

import com.TimeVenture.model.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Project findByPid(Integer projectId);

    @Query("SELECT p FROM Project p JOIN p.projectMembers pm WHERE pm.member.email = :mid")
    List<Project> findByProjectMembers(@Param("mid") String mid);
}
