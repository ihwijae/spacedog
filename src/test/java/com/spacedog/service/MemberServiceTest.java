package com.spacedog.service;

import com.spacedog.member.exception.MemberException;
import com.spacedog.member.repository.MemberRepository;
import com.spacedog.member.service.LoginRequest;
import com.spacedog.member.service.MemberService;
import com.spacedog.member.service.MemberSignUpRequest;
import com.spacedog.member.service.MemberSignUpResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {


    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceTest(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @Test
    @Transactional
    public void 회원가입() {

        //given
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest();

        memberSignUpRequest.setEmail("test@test.com");
        memberSignUpRequest.setName("test");
        memberSignUpRequest.setPassword("123456789");
        memberSignUpRequest.setCheckedPassword("123456789");
        memberSignUpRequest.setNickname("테스트닉네임");

        //when
        MemberSignUpResponse memberSignUpResponse = memberService.singUp(memberSignUpRequest);
        //then

    }

    @Test
    @Transactional
    public void 회원가입중복예외() {


        //given
        MemberSignUpRequest request1 = new MemberSignUpRequest();
        request1.setEmail("test@sss.com");
        request1.setPassword("123456789");
        request1.setCheckedPassword("123456789");

        MemberSignUpRequest request2 = new MemberSignUpRequest();
        request2.setEmail("test@sss.com");
        request2.setPassword("123456789");
        request2.setCheckedPassword("123456789");


        //when
        memberService.singUp(request1);

        //then
        assertThrows(MemberException.EmailDuplicateException.class, () -> {
            memberService.singUp(request2);
        });

    }

    @Test
    @Transactional
    public void 로그인테스트 () {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setEmail("bbb@bbb.com");
        loginRequest.setPassword("1234567833");

        memberService.login(loginRequest);

    }

}

