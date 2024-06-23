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

    private final WebClient webClient;

    public KakaoUtils(WebClient.Builder webClientBuilder,
                      @Value("${accessTokenUrl}") String accessTokenUrl) {
        log.info("accessTokenUrl={}", accessTokenUrl);
        this.webClient = webClientBuilder.baseUrl(accessTokenUrl).build();
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

        // WebClient를 사용하여 HTTP POST 요청을 보내고 응답을 비동기적으로 처리
        String accessToken = webClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Map.class) // Map으로 응답을 받음
                .map(response -> (String) response.get("access_token")) // access_token 추출
                .block();

        if (accessToken != null) {
            return accessToken;
        } else {
            throw new CustomKakaoException("엑세스토큰을 가져오는데 실패했습니다.");
        }
    }
}
