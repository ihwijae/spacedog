package com.spacedog.member.service;

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



}
