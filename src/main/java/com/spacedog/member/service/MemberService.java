package com.spacedog.member.service;

import com.spacedog.cart.domain.Cart;
import com.spacedog.cart.repository.CartRepository;
import com.spacedog.member.domain.Member;
import com.spacedog.member.exception.MemberException;
import com.spacedog.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidate memberValidate;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;


    @Transactional
    public MemberSignUpResponse singUp(MemberSignUpRequest req) {
        Member member = new Member();
        member.memberValidate(memberValidate, req);

        // 비밀번호 암호화
        req.setPassword(passwordEncoder.encode(req.getPassword()));

        Member save = memberRepository.save(MemberMapper.INSTANCE.toEntity(req));

        //장바구니 생성
        Cart cart = Cart.builder()
                .id(save.getId())
                .build();

        cartRepository.save(cart);


        return MemberMapper.INSTANCE.toSignUpResponse(save);
    }

    //내 정보 조회
    @Transactional(readOnly = true)
    public MemberResponse findByDetailMyInfo() {
        Member member = getMember();

        return MemberMapper.INSTANCE.toMemberResponse(member);

    }

    //회원 수정
    @Transactional
    public MemberEditResponse memberEdit(MemberEditRequest request) {
        Member member = getMember(); //JPA 영속성 컨텍스트 1차캐시에 저장

        if(member == null) {
            throw new MemberException("로그인한 사용자의 정보를 가져올 수 없습니다");
        }

        return member.memberEdit(request, passwordEncoder);
    }


    @Transactional(readOnly = true)
    public List<MemberFindAllResponse> memberFindAll() {
        List<Member> allMember = memberRepository.findAll();

        return allMember.stream()
                .map(mem -> new MemberFindAllResponse(mem.getName(), mem.getEmail()))
                .collect(Collectors.toList());

    }


    public LoginResponse login(LoginRequest loginRequest) {

        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new MemberException.EmailDuplicateException("가입 되지 않은 이메일 입니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new MemberException.PassWordDuplicateException("비밀번호가 틀렸습니다 다시 입력하세요.");
        }


        return new LoginResponse(member.getEmail(), "정상적으로 로그인 됐습니다.");
    }


    public  Member getMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
      return  memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException.EmailDuplicateException("해당 계정을 찾을 수 없습니다"));
    }
}



