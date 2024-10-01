package com.jwtmember.member.repository;

import com.jwtmember.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

//
    boolean existsByEmail(String email);
    boolean existsByNickName(String username);

    Optional<Member> findByEmail(String email);
}
