package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.zerock.mallapi.dto.MemberDto;
import org.zerock.mallapi.service.MemberService;
import org.zerock.mallapi.util.JWTUtil;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member/kakao")
public class KakaoLoginController {
    private final MemberService memberService;

    /**
     * 사용자가 카카오로 로그인하기 버튼을 눌렀을 때
     * @return 카카오 로그인 페이지 리턴, 로그인 후 동의 페이지 출력
     */
    @GetMapping("/authentication")
    public RedirectView kakaoLoginView() {
        return new RedirectView(memberService.getKakaoURI());
    }

    /**
     * 사용자가 카카오 로그인 후 동의 페이지에서 동의 버튼을 눌렀을 때,
     * @param authorCode 카카오 인가 코드
     * @return jwt accessToken, refreshToken 반환
     */
    @GetMapping
    public Map<String, Object> kakaoRedirect(@RequestParam(name = "code") String authorCode) {
        String kakaoAccessToken = memberService.getAccessToken(authorCode);
        MemberDto memberDto = memberService.getKakaoMember(kakaoAccessToken);

        Map<String, Object> claims = memberDto.getClaims();

        String jwtAccessToken = JWTUtil.generateToken(claims, 10);
        String jwtRefreshToken = JWTUtil.generateToken(claims, 24 * 60);

        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jwtRefreshToken);

        return claims;
    }
}
