package com.spacedog.member.domain;

import com.spacedog.member.service.MemberSignUpRequest;
import com.spacedog.member.service.MemberValidate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity{


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true)
    private String email;

    private String password;
    private String name;
    private LocalDate birthDate;
    private String nickName;

    @Embedded
    private Address address;
//


    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Authority authority;




    public void memberValidate (MemberValidate memberValidate, MemberSignUpRequest request) {
        memberValidate.emailDuplicate(request.getEmail());
        memberValidate.nickNameDuplicate(request.getNickname());
        memberValidate.passwordDuplicate(request);
    }



}
