package com.TimeVenture.service.member.email.impl;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//로그인 진행시 사용자 정보를 가져오는 서비스
@RequiredArgsConstructor
@Service
@Slf4j
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        
        //email을 통해서 Member Entity를 가져옴
       Member member  =  memberRepository.findByEmail(username).get();

       MemberDetails memberDetails = new MemberDetails(member);

       return  memberDetails;
    }
}
