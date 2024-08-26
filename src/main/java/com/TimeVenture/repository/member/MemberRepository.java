package com.TimeVenture.repository.member;

import com.TimeVenture.model.entity.member.Member;
import com.TimeVenture.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmail(String email);

    Member findByRole(Role role);

    boolean existsByEmail(String email);


}
