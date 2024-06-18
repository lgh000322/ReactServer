package org.zerock.mallapi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoUtils {

    @Value("${kakao.url}")
    private String url;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectURI;

    @Value("${kakao.response.type}")
    private String responseType;

    //첫번째 인가코드를 가져오는 메소드
    public String getKakaoURI() {
        return url + "?client_id=" + clientId + "&redirect_uri=" + redirectURI + "&response_type=" + responseType;
    }

}
