package com.TimeVenture.service.member;

import com.TimeVenture.model.dto.member.ResponseMemberDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.repository.member.MemberRepository;
import com.TimeVenture.repository.project.ProjectRepository;
import com.TimeVenture.repository.projectMember.ProjectMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberStringService {

    private final MemberRepository memberRepository;

    private final ProjectMemberRepository projectMemberRepository;

    private final ProjectRepository projectRepository;

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with email: " + email));
    }

    public ResponseMemberDto getMember(String memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid MemberId : " + memberId));
        return new ResponseMemberDto(member);
    }

}
