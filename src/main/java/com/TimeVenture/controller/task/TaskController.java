package com.TimeVenture.controller.task;

import com.TimeVenture.model.TaskModelMapper;
import com.TimeVenture.model.dto.task.CreateTaskRequestDto;
import com.TimeVenture.model.dto.task.ResponseTaskDto;
import com.TimeVenture.model.dto.task.UpdateTaskRequestDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.model.entity.task.Task;
import com.TimeVenture.model.enums.TaskSort;
import com.TimeVenture.model.enums.TaskStatus;
import com.TimeVenture.service.member.MemberStringService;
import com.TimeVenture.service.project.ProjectService;
import com.TimeVenture.service.projectMember.ProjectMemberService;
import com.TimeVenture.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final MemberStringService memberService;
    private final ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<ResponseTaskDto>> getTasksByMid(@RequestParam("memberId") String mid) {
        System.out.println("Received mid: " + mid); // mid 값을 확인합니다.
        Member member = memberService.findByEmail(mid);
        List<ResponseTaskDto> responseDtos = taskService.getTasksByMid(member);
        if (responseDtos.isEmpty()) {
            return ResponseEntity.ok().body(Collections.emptyList());
        }
        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping("/project/{pid}")
    public ResponseEntity<List<ResponseTaskDto>> getTasksByProjectId(@PathVariable("pid") int pid) {
        Project project = projectService.findById(pid);
        List<ResponseTaskDto> responseDtos = taskService.getTasksByPid(project);
        return ResponseEntity.ok().body(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseTaskDto> getTaskById(@PathVariable("id") int id) {
        ResponseTaskDto responseDto = taskService.getTaskById(id);
        return ResponseEntity.ok().body(responseDto);
    }
    @GetMapping("/search")
    public ResponseEntity<List<ResponseTaskDto>> searchTasks(
            @RequestParam(required = false) String searchWord,
            @RequestParam(required = false) Integer pid,
            @RequestParam(required = false) String mid,
            @RequestParam(required = false) String status) {
        List<ResponseTaskDto> tasks;

        if (pid != null && mid != null && status != null) {
            // 프로젝트 ID, 멤버 ID, 상태가 모두 제공된 경우
            Project project = projectService.findById(pid);
            Member member = memberService.findByEmail(mid);
            TaskStatus taskStatus = TaskStatus.valueOf(status);
            tasks = taskService.search1(searchWord, project, taskStatus, member);
        } else if (pid != null && status != null) {
            // 프로젝트 ID와 상태가 제공된 경우
            Project project = projectService.findById(pid);
            TaskStatus taskStatus = TaskStatus.valueOf(status);
            tasks = taskService.search2(searchWord, project, taskStatus);
        } else if (pid != null) {
            // 프로젝트 ID만 제공된 경우
            Project project = projectService.findById(pid);
            tasks = taskService.search3(searchWord, project);
        } else if (status != null) {
            // 상태만 제공된 경우
            TaskStatus taskStatus = TaskStatus.valueOf(status);
            tasks = taskService.search4(searchWord, taskStatus);
        } else {
            // 검색어만 제공된 경우
            tasks = taskService.findByTitleContaining(searchWord);
        }

        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<ResponseTaskDto> createTask(@RequestBody CreateTaskRequestDto requestDto) {
        ResponseTaskDto createdTask = taskService.createTask(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseTaskDto> updateTask(@PathVariable("id") int id, @RequestBody UpdateTaskRequestDto requestDto) {
        ResponseTaskDto responseDto = taskService.updateTask(id, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") int id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

}