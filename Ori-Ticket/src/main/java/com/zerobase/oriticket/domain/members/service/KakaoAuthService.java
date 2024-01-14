package com.zerobase.oriticket.domain.members.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.oriticket.domain.members.model.KakaoProfile;
import com.zerobase.oriticket.domain.members.model.OAuthToken;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class KakaoAuthService {

    public static final String KAKAO_OAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    public static final String KAKAO_USER_ME_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String CONTENT_TYPE_HEADER = "Content-type";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    private static final String CLIENT_ID = "0f5dbcca74a5d4028b0110d4e1201c8d";
    private static final String REDIRECT_URI ="https://ori-ticket.vercel.app/members/kakao/login";
//    private static final String REDIRECT_URI ="http://localhost:8080/members/kakao/login";


    private RestTemplate restTemplate = new RestTemplate();

    public OAuthToken requestKakaoToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE_HEADER, APPLICATION_X_WWW_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE_AUTHORIZATION_CODE);
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_OAUTH_TOKEN_URL, HttpMethod.POST,
                kakaoTokenRequest, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;

        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return oauthToken;
    }

    public ResponseEntity<String> requestKakaoProfile(OAuthToken oAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, "Bearer " + oAuthToken.getAccess_token());
        headers.add(CONTENT_TYPE_HEADER, APPLICATION_X_WWW_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        return restTemplate.exchange(KAKAO_USER_ME_URL, HttpMethod.POST, kakaoProfileRequest, String.class);
    }

    public KakaoProfile registerOrUpdateKakaoUser(ResponseEntity<String> kakaoProfileResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;

        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return kakaoProfile;
    }
}



