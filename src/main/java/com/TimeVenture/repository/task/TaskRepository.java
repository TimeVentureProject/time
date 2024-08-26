package com.TimeVenture.repository.task;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.model.entity.task.Task;
import com.TimeVenture.model.enums.TaskStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>{
    // #{#entityName} : Spring Data JPA 에서 제공하는 SpEL(Spring Expression Language)의 표현식. T로 대체된다.: Spring Data JPA 에서 제공하는 SpEL(Spring Expression Language)의 표현식. T로 대체된다.
    @Query("SELECT t FROM Task t WHERE t.pid = :id")
    List<Task> findAllByPid(@Param("id") Project id);

    List<Task> findByPidAndTaskStatus(Project pid, TaskStatus taskStatus, Sort sort);

    List<Task> findByPid(Project pid);

    List<Task> findByMid(Member mid);

    List<Task> findByTitleContaining(String searchWord);

    List<Task> findByTitleContainingAndPidAndTaskStatusAndMid(String searchWord, Project project, TaskStatus taskStatus, Member member);

    List<Task> findByTitleContainingAndPidAndTaskStatus(String searchWord, Project project, TaskStatus taskStatus);

    List<Task> findByTitleContainingAndPid(String searchWord, Project project);

    List<Task> findByTitleContainingAndTaskStatus(String searchWord, TaskStatus status);
}
