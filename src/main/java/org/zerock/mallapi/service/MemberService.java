package org.zerock.mallapi.service;

import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.dto.MemberDto;

import java.util.stream.Collectors;

public interface MemberService {

    MemberDto getKakaoMember(String accessToken);

    String getKakaoURI();

    String getAccessToken(String authorCode);

    default MemberDto entitytoDto(Member member) {
        return new MemberDto(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList())
        );
    }
}
