package com.TimeVenture.controller.projectInvite;

import com.TimeVenture.model.dto.member.InviteEmailDTO;
import com.TimeVenture.model.dto.projectInvite.ProjectInviteDto;
import com.TimeVenture.service.member.MemberService;
import com.TimeVenture.service.member.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/invite")
public class ProjectInvite {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> sendInvitation(@RequestBody ProjectInviteDto projectInviteDto) throws MessagingException {
        emailService.sendProjectInviteEmail(projectInviteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}