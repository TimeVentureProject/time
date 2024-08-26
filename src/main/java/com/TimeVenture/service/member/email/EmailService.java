package com.TimeVenture.service.member.email;

import com.TimeVenture.model.dto.member.InviteEmailDTO;
import com.TimeVenture.model.dto.projectInvite.ProjectInviteDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import jakarta.mail.MessagingException;

public interface EmailService {

    public void sendCheckEmail(String email, String token) throws MessagingException;

    //email에 담겨질 데이터와 이메일에 보여줄 form데이터
    public String setContext(String token, String email);

    //초대 이메일 전송
    public void sendInviteEmail(InviteEmailDTO inviteEmailDTO) throws MessagingException;


    public void sendInvite(Member managerEmail, Project projectId, String projectName, String email) throws MessagingException;

    //초대 이메일 세팅
    public String setInviteContext(Member managerEmail, Project projectId, String email, String projectName);

    //프로젝트 안에서 초대 이메일 전송
    public void sendProjectInviteEmail(ProjectInviteDto projectInviteDto) throws MessagingException;
}
