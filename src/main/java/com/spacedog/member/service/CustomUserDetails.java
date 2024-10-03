package com.spacedog.member.service;

import com.spacedog.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

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
               return  memberRepository.findById(memberId)
                       .map(member -> member.getAuthority().name())
                       .orElseThrow(() -> new UsernameNotFoundException("유저의 권한을 불러올 수 없습니다"));
           }
       });

       return collection;
    }

    @Override
    public String getPassword() {
//        return member.getPassword();
      return memberRepository.findById(memberId)
                .map(member -> member.getPassword())
                .orElseThrow(() -> new UsernameNotFoundException("비밀번호를 꺼내올 수 없습니다"));

    }

    @Override
    public String getUsername() {

        return  memberRepository.findById(memberId)
                .map(member -> member.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("유저 정보를 불러올 수 없습니다"));

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
