package com.TimeVenture.service.member.email;

import com.TimeVenture.model.dto.member.email.JoinRequestDto;
import com.TimeVenture.model.dto.task.CreateTaskRequestDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.service.member.email.exception.DuplicateEmailException;
import org.springframework.web.multipart.MultipartFile;

public interface JoinService {

    public Member join(JoinRequestDto joinRequestDto) throws DuplicateEmailException;

    //프로필 업로드
    public String getFileExtension(String imgName);

    //프로필 업로드
    public String saveProfileImg(MultipartFile img);

    //초대 받은 회원이 회원가입을 했을 경우
    public void existMemberUpdate(String email, Integer projectId);

    //테스크 추가
    public void newTask(CreateTaskRequestDto createTaskRequestDto, String taskTitle);
}
