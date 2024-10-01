package com.jwtmember.member.apicontroller;

import com.jwtmember.member.service.MemberFindAllResponse;
import com.jwtmember.member.service.MemberService;
import com.jwtmember.member.service.MemberSignUpRequest;
import com.jwtmember.member.service.MemberSignUpResponse;
import com.jwtmember.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MemberController {

    private final MemberService memberService;




    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    public MemberSignUpResponse signUp(@RequestBody @Valid MemberSignUpRequest request) {
        log.info("회원가입 시작");
        MemberSignUpResponse memberSignUpRsp = memberService.singUp(request);
        return memberSignUpRsp;
    }

//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.OK)
//    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
//        return memberService.login(request);
//    }


    // 이메일 중복 검증 api
    @GetMapping("/members/email/{email}")
    public String emailDuplicate(@PathVariable String email) {
        memberService.emailDuplicate(email);
        return "이메일 사용이 가능합니다";
    }

    // 닉네임 중복 검증 api
    @GetMapping("/members/nickName/{nickName}")
    public String nickNameDuplicate(@PathVariable String nickName) {
        memberService.nickNameDuplicate(nickName);
        return "닉네임 사용이 가능합니다";
    }

    @GetMapping("/members")
    public List<MemberFindAllResponse> memberFindAll() {
        return memberService.memberFindAll();
    }




}
