package com.TimeVenture.model.dto.file;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.task.Task;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class FileDto {
    private int fileId;
    private Task tid;
    private Member mid;
    private Member pid;
    private String fileName;
    private String fileUrl;
    private Timestamp regDate;
}

