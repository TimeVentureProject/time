package com.TimeVenture.controller.project;

import com.TimeVenture.model.dto.member.google.SessionDTO;
import com.TimeVenture.model.dto.project.AddProjectRequestDto;
import com.TimeVenture.model.dto.task.CreateTaskRequestDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.service.member.email.JoinService;
import com.TimeVenture.service.project.ProjectService;
import com.TimeVenture.service.projectMember.ProjectMemberService;
import com.TimeVenture.service.task.TaskService;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProjectStartController {
    private final ProjectService projectService;

    private final ProjectMemberService projectMemberService;

    private final TaskService taskService;

    private final JoinService joinService;

    //프로젝트 접근
    @GetMapping("/createProject")
    public String create() {
        return "projectStart/createProject";
    }

    @PostMapping("/makeProject")
    public String make(@RequestParam("pName") String pName, AddProjectRequestDto requestDto, Model model, HttpServletRequest request) throws MessagingException {
        Project project = projectService.save(requestDto);
        HttpSession session = request.getSession();
        SessionDTO member = (SessionDTO) session.getAttribute("member");

        model.addAttribute("mId", member.getEmail());
        model.addAttribute("pId", project.getPid());
        return "projectStart/createTask";
    }

    @PostMapping("/makeTask")
    public String makeTask(CreateTaskRequestDto requestDto,
                           @RequestParam("task1") String task1,
                           @RequestParam("task2") String task2,
                           @RequestParam("task3") String task3,
                           Model model) {
        joinService.newTask(requestDto, task1);
        joinService.newTask(requestDto, task2);
        joinService.newTask(requestDto, task3);
        return "common/main";
    }
}
