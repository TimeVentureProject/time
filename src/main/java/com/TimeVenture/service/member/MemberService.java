package com.TimeVenture.service.member;

import com.TimeVenture.model.dto.member.ResponseMemberDto;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.entity.project.Project;
import com.TimeVenture.model.entity.projectMember.ProjectMember;
import com.TimeVenture.repository.member.MemberRepository;
import com.TimeVenture.repository.project.ProjectRepository;
import com.TimeVenture.repository.projectMember.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final ProjectMemberRepository projectMemberRepository;

    private final ProjectRepository projectRepository;

    public List<ResponseMemberDto> getAllMembers(String Project) {
        Project project = projectRepository.findByPid(Integer.parseInt(Project));
        List<ProjectMember> projectMembers =  projectMemberRepository.findAllByProject(project);
        List<ResponseMemberDto> pMembers = new ArrayList<>();
        for(int i=0; i<projectMembers.size(); i++) {
            Member members = projectMembers.get(i).getMember();
            String auth = projectMembers.get(i).getAuth().name();
            ResponseMemberDto responseMemberDto = new ResponseMemberDto(members, auth);
            pMembers.add(responseMemberDto);
        }
        return pMembers;
    }

    public ResponseMemberDto getMember(String memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid MemberId : " + memberId));
        return new ResponseMemberDto(member);
    }

    public Member updateNickname(String email, String newNickname) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID : " + email));
        member.setName(newNickname);
        return memberRepository.save(member);
    }

}
