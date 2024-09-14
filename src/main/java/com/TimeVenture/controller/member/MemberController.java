package com.TimeVenture.controller.member;

import com.TimeVenture.model.dto.member.ResponseMemberDto;
import com.TimeVenture.model.dto.member.UpdateNicknameRequest;
import com.TimeVenture.model.dto.member.google.SessionDTO;
import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{projectId}")
    public ResponseEntity<List<ResponseMemberDto>> findAllMembers(@PathVariable("projectId") String projectId) {
        List<ResponseMemberDto> members = memberService.getAllMembers(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(members);
    }
    @PostMapping("/updateNickname")
    public ResponseEntity<Member> updateNickname(HttpSession session, @RequestBody UpdateNicknameRequest request) {
        // 세션에서 이메일 가져오기
        SessionDTO member = (SessionDTO) session.getAttribute("member");
        String email = member.getEmail();
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        try {
            Member updatedMember = memberService.updateNickname(email, request.getNewNickname());
            return ResponseEntity.ok().body(updatedMember);
        } catch (Exception e) {
            // 로그 추가
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "닉네임 변경 실패");
        }
    }
}