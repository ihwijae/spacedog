package com.spacedog.member.service;

import com.spacedog.member.domain.Address;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditResponse {



    private String password;

    private String nickName;

    @Embedded
    private Address address;

    private String message;


}
