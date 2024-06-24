package org.zerock.mallapi.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@Slf4j
public class KakaoUtils {
    @Value("${kakao.url}")
    private String url;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectURI;

    @Value("${kakao.response.type}")
    private String responseType;

    private final WebClient webClient1;

    private final WebClient webClient2;

    public KakaoUtils(WebClient.Builder webClientBuilder,
                      @Value("${accessTokenUrl}") String accessTokenUrl,
                      @Value("${kakao.user.info.url}") String userInfoURL) {
        log.info("accessTokenUrl={}", accessTokenUrl);
        this.webClient1 = webClientBuilder.baseUrl(accessTokenUrl).build();
        this.webClient2 = webClientBuilder.baseUrl(userInfoURL).build();
    }

    // 인가 코드 URL 생성
    public String getKakaoURI() {
        log.info("url={}", url);
        return url + "?client_id=" + clientId + "&redirect_uri=" + redirectURI + "&response_type=" + responseType;
    }

    // 액세스 토큰 요청 및 처리
    public String getKakaoAccessToken(String authorizationCode) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectURI);
        formData.add("code", authorizationCode);

        String accessToken = webClient1.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"))
                .block();

        if (accessToken != null) {
            return accessToken;
        } else {
            throw new CustomKakaoException("엑세스토큰을 가져오는데 실패했습니다.");
        }
    }

    //카카오 유저 정보 가져오기(id)
    public Long getKakaoUserInfo(String accessToken) {
        Long id = webClient2.get()
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Long) response.get("id"))
                .block();

        if (id != null && id != 0) {
            return id;
        } else {
            throw new CustomKakaoException("카카오 유저정보를 가져오는데 오류가 발생했습니다.");
        }
    }
}
