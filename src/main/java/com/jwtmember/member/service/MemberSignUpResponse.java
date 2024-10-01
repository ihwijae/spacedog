package com.jwtmember.member.service;

import com.jwtmember.member.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberSignUpResponse {


    private Long id;
    private String email;
    private String username;
    private String nickname;
    private String birthdate;


    public static MemberSignUpResponse toDto(Member member) {
        return MemberSignUpResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getName())
                .nickname(member.getNickName())
                .build();
    }
}
