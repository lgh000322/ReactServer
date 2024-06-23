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

    @Value("${kakao.access.token}")
    //인가 코드 url
    public String getKakaoURI() {
        return url + "?client_id=" + clientId + "&redirect_uri=" + redirectURI + "&response_type=" + responseType;
    }

}
