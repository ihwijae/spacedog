package com.spacedog.member.service;

import com.spacedog.member.domain.Address;
import com.spacedog.member.domain.Authority;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MemberResponse {


    private String email;

    private String name;
    private LocalDate birthDate;
    private String nickName;

    @Embedded
    private Address address;


    private Authority authority;

}
