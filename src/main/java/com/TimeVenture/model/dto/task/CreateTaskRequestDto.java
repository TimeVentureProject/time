package com.TimeVenture.model.dto.task;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.model.entity.task.Task;
import com.TimeVenture.model.enums.Priority;
import com.TimeVenture.model.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CreateTaskRequestDto {
    private int pid;
    private String mid;
    private ProjectMember pmember;
    private String title;
    private String content;
    private Priority priority;
    private TaskStatus taskStatus;
    private Timestamp dueDate;

    public Task toEntity(Project project, Member member) {
        return Task.builder()
                .title(title)
                .content(content)
                .dueDate(dueDate)
                .priority(priority)
                .taskStatus(taskStatus)
                .pmember(pmember)
                .pid(project)
                .mid(member)
                .build();
    }
}