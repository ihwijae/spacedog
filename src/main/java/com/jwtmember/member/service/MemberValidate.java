package com.jwtmember.member.service;

import com.jwtmember.exception.MemberException;
import com.jwtmember.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidate {


    private final MemberRepository memberRepository;


    public void emailDuplicate(String email) {
        boolean result = memberRepository.existsByEmail(email);

        if(result) {
            throw new MemberException.EmailDuplicateException("중복된 이메일 입니다.");
        }
    }


    public void nickNameDuplicate(String nickName) {
        boolean result = memberRepository.existsByNickName(nickName);

        if(result) {
            throw new MemberException.NickNameDuplicateException("중복된 닉네임 입니다 다른 닉네임을 선택하세요");
        }
    }
}
