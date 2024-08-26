package com.TimeVenture.model.dto.review;

import com.TimeVenture.model.dto.member.MemberDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import lombok.Data;

import java.sql.Timestamp;

    @Data
    public class ReviewsDto {
        private int reviewId;
        private Project pid;
        private MemberDto mid;
        private String content;
        private Timestamp createdDate;
    }

