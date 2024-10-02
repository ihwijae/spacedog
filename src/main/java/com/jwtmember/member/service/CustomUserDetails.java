package com.jwtmember.member.service;

import com.jwtmember.member.domain.Member;
import com.jwtmember.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long memberId;
    private final MemberRepository memberRepository;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

       Collection<GrantedAuthority> collection = new ArrayList<>();

       collection.add(new GrantedAuthority() {

           @Override
           public String getAuthority() {
//               return member.getAuthority().name();
               Member member = memberRepository.findById(memberId)
                       .orElseThrow(() -> new UsernameNotFoundException("유저의 권한을 불러올 수 없습니다"));
               return member.getAuthority().name();

           }
       });

       return collection;
    }

    @Override
    public String getPassword() {
//        return member.getPassword();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("비밀번호를 꺼내올 수 없습니다"));
        return member.getPassword();
    }

    @Override
    public String getUsername() {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("유저 정보를 불러올 수 없습니다"));
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
