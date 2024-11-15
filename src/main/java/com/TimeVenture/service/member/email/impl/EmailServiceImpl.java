package com.TimeVenture.service.member.email.impl;

import com.TimeVenture.model.dto.member.InviteEmailDTO;
import com.TimeVenture.model.dto.projectInvite.ProjectInviteDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.repository.member.MemberRepository;
import com.TimeVenture.repository.project.ProjectRepository;
import com.TimeVenture.service.member.email.EmailService;
import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    
    //인증 이메일 전송
    @Override
    public void sendCheckEmail(String email, String token) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        try {
            helper.setTo(email);
            helper.setSubject("TimeVenture 가입을 완료해주세요.");

            // 리소스 직접 로드
            Resource resource = new ClassPathResource("static/image/common/logo.png");

            // MimeBodyPart를 사용하여 이미지 첨부
            MimeBodyPart imageBodyPart = new MimeBodyPart();
            ByteArrayDataSource ds = new ByteArrayDataSource(resource.getInputStream(), "image/png");
            imageBodyPart.setDataHandler(new DataHandler(ds));
            imageBodyPart.setHeader("Content-ID", "<logoImage>");

            // Multipart 메시지 생성
            MimeMultipart multipart = new MimeMultipart("related");

            // HTML 콘텐츠 추가
            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = setContext(token, email);
            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");

            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(imageBodyPart);

            mimeMessage.setContent(multipart);

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new MessagingException("Failed to send email: " + e.getMessage());
        }
    }

    //email에 담겨질 데이터와 이메일에 보여줄 form데이터
    @Override
    public String setContext(String token, String email) {
        Context context = new Context();
        context.setVariable("token", token);
        context.setVariable("email", email);
        return templateEngine.process("member/emailAuthentication", context);
    }

    //회원가입시 초대 이메일 전송
    @Override
    public void sendInviteEmail(InviteEmailDTO inviteEmailDTO) throws MessagingException {
        Member managerEmail = inviteEmailDTO.getManagerEmail();
        Project projectId = inviteEmailDTO.getProjectId();
        String projectName = inviteEmailDTO.getProjectName();
        if(inviteEmailDTO.getEmail1()!=null) {
            sendInvite(managerEmail, projectId, projectName, inviteEmailDTO.getEmail1());
        }else if (inviteEmailDTO.getEmail2()!=null) {
            sendInvite(managerEmail, projectId, projectName,inviteEmailDTO.getEmail2());
        }else if(inviteEmailDTO.getEmail3()!=null) {
            sendInvite(managerEmail, projectId, projectName,inviteEmailDTO.getEmail3());
        }else if(inviteEmailDTO.getEmail4()!=null) {
            sendInvite(managerEmail, projectId, projectName,inviteEmailDTO.getEmail4());
        }
    }
    
    //초대 이메일 전송
    @Override
    public void sendInvite(Member managerEmail, Project projectId, String projectName, String email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.addRecipients(MimeMessage.RecipientType.TO, email);
        mimeMessage.setSubject(managerEmail.getEmail() + " 님이 TimeVenture에 초대하셨습니다.");
        mimeMessage.setText(setInviteContext(managerEmail, projectId, email, projectName), "utf-8", "html");
        mailSender.send(mimeMessage);
    }

    //초대 이메일 세팅
    @Override
    public String setInviteContext(Member managerEmail, Project projectId, String email, String projectName) {
        Context context = new Context();
        context.setVariable("managerEmail", managerEmail.getEmail());
        context.setVariable("projectId", projectId.getPid());
        context.setVariable("email", email);
        context.setVariable("projectName", projectName);
        return templateEngine.process("member/emailInvitation", context);
    }

    //회원가입시 초대 이메일 전송
    @Override
    public void sendProjectInviteEmail(ProjectInviteDto projectInviteDto) throws MessagingException {
        String managerEmail = projectInviteDto.getManagerEmail();
        int projectId = Integer.parseInt(projectInviteDto.getProjectId());
        Member email = memberRepository.findByEmail(managerEmail).get();
        Project proId = projectRepository.findByPid(projectId);
        String projectName = projectInviteDto.getProjectName();
        if(projectInviteDto.getEmail()!=null) {
            sendInvite(email, proId, projectName, projectInviteDto.getEmail());
        }
    }
}