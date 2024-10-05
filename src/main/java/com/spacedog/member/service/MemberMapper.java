package com.spacedog.member.service;

import com.spacedog.member.domain.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper //어노테이션 붙이면 mapstruct가 자동으로 MemberMapper 구현체 생성해준다.
public interface MemberMapper {


    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(source = "memberSignUpRequest.email", target = "email")
    @Mapping(source = "memberSignUpRequest.password", target = "password")
    @Mapping(source = "memberSignUpRequest.name", target = "name")
    @Mapping(source = "memberSignUpRequest.nickname", target = "nickName")
    @Mapping(target = "authority", expression = "java(com.spacedog.member.domain.Authority.ROLE_USER)")
    Member toEntity(MemberSignUpRequest memberSignUpRequest);


    @Mapping(source = "member.id", target = "id")
    @Mapping(source = "member.email", target = "email")
    @Mapping(source = "member.name", target = "username")
    @Mapping(source = "member.nickName", target = "nickname")
    MemberSignUpResponse toSignUpResponse(Member member);



    MemberResponse toMemberResponse(Member member);


    @Mapping(target = "message", constant = "정상적으로 수정이 완료 됐습니다")
    MemberEditResponse toMemberEditResponse(Member member);
}
