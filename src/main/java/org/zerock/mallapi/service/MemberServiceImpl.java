package org.zerock.mallapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.MemberRole;
import org.zerock.mallapi.dto.MemberDto;
import org.zerock.mallapi.repository.MemberRepository;
import org.zerock.mallapi.util.KakaoUtils;

import javax.swing.text.html.Option;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoUtils kakaoUtils;
    @Transactional
    @Override
    public MemberDto getKakaoMember(String accessToken) {
        String nickname = kakaoUtils.getKakaoUserInfo(accessToken);
        log.info("======================================================");
        log.info("카카오 유저의 nickname={}", nickname);

        Optional<Member> result = memberRepository.findById(nickname);

        //이미 가입했다면
        if (result.isPresent()) {
            return entitytoDto(result.orElseThrow());
        }

        //email대신 nickname을 email로 주게끔 구현
        Member socialMember = makeSocialMember(nickname);
        memberRepository.save(socialMember);
        return entitytoDto(socialMember);
    }

    @Override
    public String getKakaoURI() {
        return kakaoUtils.getKakaoURI();
    }

    @Override
    public String getAccessToken(String authorCode) {
        return kakaoUtils.getKakaoAccessToken(authorCode);
    }

    private String makeTempPassword() {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < 10; i++) {
            buffer.append((char) ((int) Math.random() * 55) + 65);
        }

        return passwordEncoder.encode(buffer.toString());
    }

    private Member makeSocialMember(String email) {

        String tempPassword = makeTempPassword();

        log.info("tempPassword={}", tempPassword);

        Member member = Member.builder()
                .email(email)
                .pw(tempPassword)
                .nickname("Social Member")
                .social(true)
                .build();

        member.addRole(MemberRole.USER);

        return member;
    }
}
