package com.jwtmember.member.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberSignUpRequest {


    @NotBlank(message = "이메일을 입력하세요")
    @Email
    private String email;

    @NotBlank(message = "패스워드를 입력하세요")
    @Length(min = 8, max = 20)
    private String password;


    @NotBlank
    private String checkedPassword;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;


    @NotBlank(message = "닉넴임을 입력하세요")
    private String nickname;
}
