package com.spacedog.member.service;

import com.spacedog.member.exception.MemberException;
import com.spacedog.member.repository.MemberRepository;
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

    public void passwordDuplicate(MemberSignUpRequest request) {
        // 비밀번호 중복 확인
        if (!request.getPassword().equals(request.getCheckedPassword())) {
            throw new MemberException.CheckedPassWordDuplicateException("비밀번호가 일치하지 않습니다.");
        }
    }
}
