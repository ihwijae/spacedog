package com.spacedog.member.domain;

import com.spacedog.member.exception.MemberException;
import com.spacedog.member.service.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    public void memberValidate(MemberValidate memberValidate, MemberSignUpRequest request) {
        memberValidate.emailDuplicate(request.getEmail());
        memberValidate.nickNameDuplicate(request.getNickname());
        memberValidate.passwordDuplicate(request);
    }

    public MemberEditResponse memberEdit(MemberEditRequest request, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(request.getPassword());
        this.nickName = request.getNickName();
        this.address = request.getAddress();

        return MemberMapper.INSTANCE.toMemberEditResponse(this);
    }

}
