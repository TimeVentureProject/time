package com.TimeVenture.model.dto.task;

import com.TimeVenture.model.enums.Priority;
import com.TimeVenture.model.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
public class UpdateTaskRequestDto {
    private String title;
    private String content;
    private Priority priority;
    private TaskStatus taskStatus;
    private Timestamp dueDate;

}