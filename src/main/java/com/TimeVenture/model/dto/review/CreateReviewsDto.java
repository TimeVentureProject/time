package com.TimeVenture.model.dto.review;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.review.Reviews;
import com.TimeVenture.model.entity.task.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewsDto {
    private int pid;
    private String mid;
    private String content;

    public Reviews toEntity(Project project, Member member) {
        return Reviews.builder()
                .pid(project)
                .mid(member)
                .content(content)
                .build();
    }
}

