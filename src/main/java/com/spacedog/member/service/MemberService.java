package com.spacedog.member.service;

import com.spacedog.member.domain.Member;
import com.spacedog.member.exception.MemberException;
import com.spacedog.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberValidate memberValidate;



    @Transactional
    public MemberSignUpResponse singUp(MemberSignUpRequest req) {
        Member member = new Member();
        member.memberValidate(memberValidate, req);

        // 비밀번호 암호화
        req.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));

        Member save = memberRepository.save(MemberMapper.INSTANCE.toEntity(req));

        return MemberMapper.INSTANCE.toSignUpResponse(save);
    }






    @Transactional
    public List<MemberFindAllResponse> memberFindAll() {
        List<Member> allMember = memberRepository.findAll();

        return allMember.stream()
                .map(mem -> new MemberFindAllResponse(mem.getName(), mem.getEmail()))
                .collect(Collectors.toList());

    }


    public LoginResponse login(LoginRequest loginRequest) {

        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow( () -> new MemberException.EmailDuplicateException("가입 되지 않은 이메일 입니다."));

        if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new MemberException.PassWordDuplicateException("비밀번호가 틀렸습니다 다시 입력하세요.");
        }


        return new LoginResponse(member.getEmail(), "정상적으로 로그인 됐습니다.");
    }
}
