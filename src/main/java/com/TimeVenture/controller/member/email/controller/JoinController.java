package com.TimeVenture.controller.member.email.controller;//package com.TimeVenture.join.controller;

import com.TimeVenture.model.dto.member.InviteEmailDTO;
import com.TimeVenture.model.dto.member.email.JoinRequestDto;
import com.TimeVenture.model.dto.project.AddProjectRequestDto;
import com.TimeVenture.model.dto.projectMember.AddProjectMemberRequestDto;
import com.TimeVenture.model.dto.task.CreateTaskRequestDto;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.service.member.email.EmailService;
import com.TimeVenture.service.member.email.JoinService;
import com.TimeVenture.service.member.email.TokenService;
import com.TimeVenture.service.member.email.exception.DuplicateEmailException;
import com.TimeVenture.service.project.ProjectService;
import com.TimeVenture.service.projectMember.ProjectMemberService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class JoinController {

    private final TokenService tokenService;

    private final EmailService emailService;

    private final JoinService joinService;

    private final ProjectService projectService;

    private final ProjectMemberService projectMemberService;

    //회원가입 접근
    @GetMapping("/join")
    public String join() {
        return "member/join";
    }

    //email전송 및 email 전송 확인 페이지 이동
    @PostMapping("/confirmEmail")
    public String confirmEmail(@RequestParam("email") String email) throws MessagingException {
        String token = tokenService.generateToken(email);
        emailService.sendCheckEmail(email, token);
        return "member/confirmEmail";
    }

    //전달받은 이메일 클릭 후 인증번호 검증 후 페이지 이동
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("email") String email, @RequestParam("token") String token, Model model) {
        if (tokenService.checkToken(email, token)) {
            // 토큰이 유효하면 회원가입 폼으로 이동
            model.addAttribute("email", email);
            return "member/joinEmailAccountSetup";
        } else {
            // 토큰이 유효하지 않으면 에러 페이지로 리다이렉트
            return "redirect:/member/join";
        }
    }

    //첫번쨰 프로젝트 생성시에 회원가입 정보를 디비에 저장
    @PostMapping("/joinAccountRole")
    public String joinProcess(JoinRequestDto joinRequestDto, Model model) throws DuplicateEmailException {
        System.out.println(joinRequestDto.toString());
        try {
            joinService.join(joinRequestDto);
            model.addAttribute("email", joinRequestDto.getEmail());
            return "member/joinAccountRole";
        } catch (DuplicateEmailException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/join";
        }
    }

    //★★★★★★★★★★★구글 회원가입과 연결되는 부분★★★★★★★★★★★
    //프로젝트 멤버 구성 임시 저장
    @PostMapping("/joinFirstProj")
    public String projectProcess(@RequestParam("email") String email,
                                @RequestParam("auth") String auth, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("auth", auth);
        return "member/joinFirstProj";
    }

    //프로젝트 생성 디비 저장
    @PostMapping("/joinNewTask")
    public String joinNewTask(AddProjectRequestDto addProjectRequestDto
                              ,AddProjectMemberRequestDto addProjectMemberRequestDto
                              ,Model model) {
        //프로젝트 생성
        Project project = projectService.save(addProjectRequestDto);
        //프로젝스 아이디 프로젝트 멤버에 세팅
        addProjectMemberRequestDto.setProjectId(project.getPid());
        //프로젝트 멤버 생성
        ProjectMember projectMember = projectMemberService.addProjectMember(addProjectMemberRequestDto);
        model.addAttribute("pid", project.getPid());
        model.addAttribute("mid", projectMember.getMember().getEmail());
        model.addAttribute("pmember",projectMember.getProjectMemberId());
        model.addAttribute("pName",project.getPname());
        return "member/joinNewTask";
    }
    @PostMapping("/sendInvitationEmail")
    public String sendInvitationEmail(CreateTaskRequestDto createTaskRequestDto,
                                      @RequestParam("pName") String projectName,
                                      @RequestParam("task1") String task1,
                                      @RequestParam("task2") String task2,
                                      @RequestParam("task3") String task3, Model model) {
        joinService.newTask(createTaskRequestDto, task1);
        joinService.newTask(createTaskRequestDto, task2);
        joinService.newTask(createTaskRequestDto, task3);
        model.addAttribute("email", createTaskRequestDto.getMid());
        model.addAttribute("projectId", createTaskRequestDto.getPid());
        model.addAttribute("projectName", projectName);
        return "member/sendInvitationEmail";
    }


    @PostMapping("/joinFinish")
    public String joinFinish(InviteEmailDTO inviteEmailDTO) throws MessagingException {
        if (inviteEmailDTO == null) {
            return "redirect:/sendInvitationEmail";
        }
        emailService.sendInviteEmail(inviteEmailDTO);
        return "member/joinFinish";
    }

}
