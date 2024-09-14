package com.TimeVenture.service.member.email.impl;

import com.TimeVenture.model.dto.member.email.JoinRequestDto;
import com.TimeVenture.model.dto.task.CreateTaskRequestDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.model.enums.Auth;
import com.TimeVenture.model.enums.Role;
import com.TimeVenture.repository.member.MemberRepository;
import com.TimeVenture.repository.project.ProjectRepository;
import com.TimeVenture.repository.projectMember.ProjectMemberRepository;
import com.TimeVenture.service.member.email.JoinService;
import com.TimeVenture.service.member.email.exception.DuplicateEmailException;
import com.TimeVenture.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JoinServiceImpl implements JoinService {

    // 사용자 저장소 인터페이스 주입
    private final MemberRepository memberRepository;

    // 비밀번호 암호화 서비스 주입
    private final PasswordEncoder passwordEncoder;

    private final ProjectRepository projectRepository;

    private final ProjectMemberRepository projectMemberRepository;

    private final TaskService taskService;
    //파일 저장경로
    private static final String UPLOAD_ROOT = "C:/upload/";

    // 회원 가입 메서드
    public Member join(JoinRequestDto joinRequestDto) throws DuplicateEmailException {

        //이메일 중복 체크
        if(memberRepository.existsByEmail(joinRequestDto.getEmail())) {
            throw new DuplicateEmailException("이미 가입된 이메일입니다.");
        }
        // 새로운 사용자 객체 생성
        Member member = Member.builder()
                .email(joinRequestDto.getEmail())
                .name(joinRequestDto.getName())
                .img(saveProfileImg(joinRequestDto.getImg()))
                .regDate(new Timestamp(System.currentTimeMillis()))
                .loginType("email")
                .role(Role.USER).pwd(passwordEncoder.encode(joinRequestDto.getPwd())).build();

        // 사용자 저장소에 사용자 저장
        return memberRepository.save(member);
    }

    //프로필 업로드
    public String getFileExtension(String imgName) {
        int dotIndex = imgName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : imgName.substring(dotIndex);
    }
    
    //프로필 업로드
    public String saveProfileImg(MultipartFile img) {
        if (img.getSize() > 100 * 1024 * 1024) { // 100MB 제한
            throw new MaxUploadSizeExceededException(100 * 1024 * 1024);
        }

        // 파일 저장
        String originalFileName = img.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String fileName = timestamp + fileExtension;
        String filePath = UPLOAD_ROOT + fileName;
        File dest = new File(filePath);

        try {
            img.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    //초대 받은 회원이 회원가입을 했을 경우
    public void existMemberUpdate(String email, Integer projectId) {
        ProjectMember projectMember = ProjectMember.builder()
                .project(projectRepository.findByPid(projectId))
                .auth(Auth.MEMBER)
                .member(memberRepository.findByEmail(email).get()).build();
        projectMemberRepository.save(projectMember);
    }

    //테스크 생성
    public void newTask(CreateTaskRequestDto createTaskRequestDto, String taskTitle) {
        taskService.joinCreateTask(createTaskRequestDto, taskTitle);
    }
}
