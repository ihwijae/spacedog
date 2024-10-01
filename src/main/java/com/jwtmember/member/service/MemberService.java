package com.jwtmember.member.service;

import com.jwtmember.member.domain.Member;
import com.jwtmember.exception.MemberException;
import com.jwtmember.member.repository.MemberRepository;
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



    @Transactional
    public MemberSignUpResponse singUp(MemberSignUpRequest req) {

       // 이메일 중복 검증
        emailDuplicate(req.getEmail());

        // 닉네임 중복 검증
        nickNameDuplicate(req.getNickname());

        // 비밀번호 중복 확인
        if(!req.getPassword().equals(req.getCheckedPassword())) {
            throw new MemberException.CheckedPassWordDuplicateException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        req.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
        log.info("paswwEncode = {} ", req.getPassword());


        Member entity = Member.toEntity(req);
        Member save = memberRepository.save(entity);

        return MemberSignUpResponse.toDto(save);
    }


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
