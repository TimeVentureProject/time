package com.TimeVenture.service.project;

import com.TimeVenture.model.dto.project.AddProjectRequestDto;
import com.TimeVenture.model.dto.project.UpdateProjectDto;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.repository.project.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public Project save(AddProjectRequestDto addProjectRequestDto) {
        return projectRepository.save(addProjectRequestDto.toEntity());
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findById(int id) {
        return projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public List<Project> findByMid(String mid) {
        return projectRepository.findByProjectMembers(mid);
    }

    public void delete(int id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    public Project update(int id, UpdateProjectDto request) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        project.update(request.getPName(),
                request.getPExplain(),
                request.getPStartDate(),
                request.getPEndDate(),
                request.getPColor());

        return project;
    }
}
