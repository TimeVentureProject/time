package com.TimeVenture.controller;

import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.service.project.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FrontController {
    private final ProjectService projectService;

    public FrontController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @GetMapping("/project/{id}")
    public String getProjectPage(@PathVariable("id") int id, Model model) {
        Project project = projectService.findById(id);
        model.addAttribute("project", project);
        return "common/project";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
    @GetMapping("/main")
    public String Main() {
        return "common/main";
    }
    @GetMapping("/calendar")
    public String getCalendarPage() {
        return "projectCalendar/calendar";
    }
    @GetMapping("/chat")
    public String getChattingPage() {
        return "projectChatting/chatting";
    }
    @GetMapping("/dash")
    public String getDashBoardPage() {
        return "projectDashBoard/dashBoard";
    }
    @GetMapping("/attachment")
    public String getAttachmentsPage() {
        return "projectFile/attachment";
    }
    @GetMapping("/home")
    public String getHomePage() {
        return "projectHome/home";
    }
    @GetMapping("/kanban")
    public String getKanbanBoardPage() {
        return "projectKanbanBoard/kanbanBoardList"; // HTML 파일 이름 (확장자 제외)
    }
    @GetMapping("/list")
    public String getListPage() {
        return "projectList/projectList"; // HTML 파일 이름 (확장자 제외)
    }
    @GetMapping("/outline")
    public String getOutlinePage() {
        return "projectStart/outline";
    }
    @GetMapping("/mypage")
    public String getmypage() {
        return "member/myPageMain";
    }

}
