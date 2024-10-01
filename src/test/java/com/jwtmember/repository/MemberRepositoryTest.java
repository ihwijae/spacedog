package com.jwtmember.repository;

import com.jwtmember.member.domain.Member;
import com.jwtmember.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @Test
    public void saveTest () {
        Member member = new Member();
        member.setEmail("test@example.com");

        memberRepository.save(member);

        memberRepository.existsByEmail("test@example.com");
    }

}