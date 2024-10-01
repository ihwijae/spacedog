package com.jwtmember.member.domain;

import com.jwtmember.member.service.MemberSignUpRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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



    public static Member toEntity(MemberSignUpRequest req){
        return Member.builder()
                .email(req.getEmail())
                .password(req.getPassword())
                .name(req.getName())
                .nickName(req.getNickname())
                .authority(Authority.ROLE_USER)
                .build();
    }

    public static Member authorization(String email, String password, Authority authority) {
        return Member.builder()
                .email(email)
                .password(password)
                .authority(authority)
                .build();
    }


}
