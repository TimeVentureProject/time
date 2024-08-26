package com.TimeVenture.service.task;

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
import com.TimeVenture.repository.member.MemberRepository;
import com.TimeVenture.repository.project.ProjectRepository;
import com.TimeVenture.repository.projectMember.ProjectMemberRepository;
import com.TimeVenture.repository.task.TaskRepository;
import com.TimeVenture.service.member.MemberStringService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {
    protected final TaskRepository taskRepository;
    protected final ProjectRepository projectRepository;
    protected final MemberRepository memberRepository;
    protected final ProjectMemberRepository projectMemberRepository;
    protected final TaskModelMapper taskModelMapper;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository, TaskModelMapper taskModelMapper, MemberStringService memberService, MemberRepository memberRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.taskModelMapper = taskModelMapper;
        this.memberRepository = memberRepository;
    }

    // CREATE
    public ResponseTaskDto createTask(CreateTaskRequestDto requestDto) {
        Project project = projectRepository.findById(requestDto.getPid())
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Member member = memberRepository.findByEmail(requestDto.getMid())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Task task = requestDto.toEntity(project, member);
        Task savedTask = taskRepository.save(task);
        return TaskModelMapper.INSTANCE.toResponseDto(savedTask);
    }

    // READ
    // 정렬 기준
    private Sort getSortCriteria(TaskSort sort) {
        Sort sortby = null; // 정렬 기준 변수 초기화
        if(sort != null) {
            sortby = switch (sort) {
                case DUE_DATE_ASC -> Sort.by(Sort.Direction.ASC, "dueDate");
                case DUE_DATE_DESC -> Sort.by(Sort.Direction.DESC, "dueDate");
                case MEMBER_ASC -> Sort.by(Sort.Direction.ASC, "pmember");
                case MEMBER_DESC -> Sort.by(Sort.Direction.DESC, "pmember");
                case CREATED_DATE_ASC -> Sort.by(Sort.Direction.ASC, "createdDate");
                case CREATED_DATE_DESC -> Sort.by(Sort.Direction.DESC, "createdDate");
                case PRIORITY_ASC -> Sort.by(Sort.Direction.ASC, "priority");
                case PRIORITY_DESC -> Sort.by(Sort.Direction.DESC, "priority");
                default -> throw new IllegalArgumentException("Unsupported sort type: " + sort);
            };
        }
        return sortby;
    }
    // 모든 tasks 조회
    public List<ResponseTaskDto> getAllTasks() {
        return taskModelMapper.toResponseDtoList(taskRepository.findAll());
    }
    public List<ResponseTaskDto> getTasksByPid(Project pid) {
        return taskModelMapper.toResponseDtoList(taskRepository.findByPid(pid));
    }
    public List<ResponseTaskDto> getTasksByMid(Member mid) {
        return taskModelMapper.toResponseDtoList(taskRepository.findByMid(mid));
    }
    // 특정 task 내용 조회
    public ResponseTaskDto getTaskById(int tId) {
        Task task = taskRepository.findById(tId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with task_id: " + tId));
        return taskModelMapper.toResponseDto(task);
    }
    // 추가: 제목으로만 검색하는 메서드
    public List<ResponseTaskDto> searchTasksByTitle(String searchWord) {
        return taskModelMapper.toResponseDtoList(taskRepository.findByTitleContaining(searchWord));
    }

    /* UPDATE */
    public ResponseTaskDto updateTask(int tid, UpdateTaskRequestDto requestDto) {
        Task task = taskRepository.findById(tid)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with task_id: " + tid));
        Task updatedTask = task.toBuilder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .priority(requestDto.getPriority())
                .taskStatus(requestDto.getTaskStatus())
                .dueDate(requestDto.getDueDate()).build();
        return taskModelMapper.toResponseDto(taskRepository.save(updatedTask));
    }

    /* DELETE */
    public void deleteTask(int tid) {
        Task task = taskRepository.findById(tid)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with task_id: " + tid));
        taskRepository.delete(task);
    }
    public void deleteTasksByPidAndTaskStatus(Project pid, TaskStatus status) {
        List<Task> tasks = taskRepository.findByPidAndTaskStatus(pid, status, null);
        taskRepository.deleteAll(tasks);
    }
    // JOINCREATE
    public ResponseTaskDto joinCreateTask(CreateTaskRequestDto requestDto, String taskTitle) {
        Project project = projectRepository.findByPid(requestDto.getPid());
        Member member = memberRepository.findByEmail(requestDto.getMid())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Task task = Task.builder()
                .pid(project)
                .mid(member)
                .pmember(requestDto.getPmember())
                .title(taskTitle)
                .content(requestDto.getContent())
                .priority(requestDto.getPriority())
                .taskStatus(requestDto.getTaskStatus())
                .build();
        Task createdTask = taskRepository.save(task);
        return taskModelMapper.toResponseDto(createdTask);
    }

    public List<ResponseTaskDto> search1(String searchWord, Project project, TaskStatus taskStatus, Member member) {
        return taskModelMapper.toResponseDtoList(taskRepository.findByTitleContainingAndPidAndTaskStatusAndMid(searchWord, project, taskStatus, member));
    }

    public List<ResponseTaskDto> search2(String searchWord, Project project, TaskStatus taskStatus) {
        return taskModelMapper.toResponseDtoList(taskRepository.findByTitleContainingAndPidAndTaskStatus(searchWord, project, taskStatus));
    }

    public List<ResponseTaskDto> search3(String searchWord, Project project) {
        return taskModelMapper.toResponseDtoList(taskRepository.findByTitleContainingAndPid(searchWord, project));
    }

    public List<ResponseTaskDto> findByTitleContaining(String searchWord) {
        return taskModelMapper.toResponseDtoList(taskRepository.findByTitleContaining(searchWord));
    }

    public List<ResponseTaskDto> search4(String searchWord, TaskStatus status) {
        return taskModelMapper.toResponseDtoList(taskRepository.findByTitleContainingAndTaskStatus(searchWord, status));
    }
}
