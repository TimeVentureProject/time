package com.TimeVenture.model.dto.member.email;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;


@Data
public class JoinRequestDto {

    private String email;

    private String name;

    private String pwd;

    private MultipartFile img;

    private Timestamp regDate;
}
