package com.TimeVenture.controller.project;

import com.TimeVenture.model.dto.project.AddProjectRequestDto;
import com.TimeVenture.model.dto.project.ResponseProjectDto;
import com.TimeVenture.model.dto.project.UpdateProjectDto;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/project")
@RestController
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> addProject(@RequestBody AddProjectRequestDto requestDto) {
        Project savedProject = projectService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @GetMapping
    public ResponseEntity<List<ResponseProjectDto>> findAllProjects() {
        List<ResponseProjectDto> projects = projectService.findAll().stream().map(ResponseProjectDto::new).toList();
        return ResponseEntity.ok().body(projects);
    }

    @GetMapping("/member")
    public ResponseEntity<List<ResponseProjectDto>> findProjectsByMid(@RequestParam("mid") String mid) {
        List<ResponseProjectDto> projects = projectService.findByMid(mid).stream().map(ResponseProjectDto::new).toList();
        return ResponseEntity.ok().body(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProjectDto> findOneProject(@PathVariable("id") int id) {
        Project project = projectService.findById(id);

        return ResponseEntity.ok().body(new ResponseProjectDto(project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseProjectDto> deleteProject(@PathVariable("id") int id) {
        projectService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") int id, @RequestBody UpdateProjectDto request) {
        Project updatedProject = projectService.update(id, request);

        return ResponseEntity.ok().body(updatedProject);
    }
}
