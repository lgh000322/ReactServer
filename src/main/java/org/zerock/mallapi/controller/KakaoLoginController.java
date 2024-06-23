package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.zerock.mallapi.util.KakaoUtils;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member/kakao")
public class KakaoLoginController {
    private final KakaoUtils kakaoUtils;
    /**
     * 사용자가 카카오로 로그인하기 버튼을 눌렀을 때
     * @return 카카오 로그인 페이지 리턴, 로그인 후 동의 페이지 출력
     */
    @GetMapping("/authentication")
    public RedirectView kakaoLoginView() {
        String kakaoURI = kakaoUtils.getKakaoURI();
        return new RedirectView(kakaoURI);
    }

    /**
     * 사용자가 카카오 로그인 후 동의 페이지에서 동의 버튼을 눌렀을 때,
     * @param authorCode 카카오 인가 코드, accessToken
     * @return
     */
    @GetMapping
    public Map<String, String> kakaoRedirect(@RequestParam(name = "code") String authorCode) {
        String kakaoAccessToken = kakaoUtils.getKakaoAccessToken(authorCode);
        return Map.of("SUCCESS", "success",
                "AuthorCode", authorCode,
                "kakaoAccessToken", kakaoAccessToken);
    }
}
