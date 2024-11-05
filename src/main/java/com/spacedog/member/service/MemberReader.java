package com.spacedog.member.service;

import com.spacedog.member.domain.Member;
import com.spacedog.member.exception.MemberException;
import com.spacedog.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberReader {


    private final MemberRepository memberRepository;


    public Member getMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return  memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException.EmailDuplicateException("해당 계정을 찾을 수 없습니다"));
    }
}
