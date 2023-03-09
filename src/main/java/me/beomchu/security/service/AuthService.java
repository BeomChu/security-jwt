package me.beomchu.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.beomchu.security.domain.User;
import me.beomchu.security.oauth.KakaoTest;
import me.beomchu.security.dto.request.SignupDto;
import me.beomchu.security.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {


    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;


    public void signup(SignupDto signupDto){

        String encodedPassword = encoder.encode(signupDto.getPassword());


        User user = User.builder()
                .username(signupDto.getUsername())
                .password(encodedPassword)
                .build();

        userRepository.save(user);


    }

    public String kakaoSignup(String code){
        //TODO 검색해볼것
        RestTemplate rt = new RestTemplate();
        //헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //바디
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id","264edb35a42e39dc36007baf5a6c82a7");
        params.add("redirect_uri","http://localhost:8080/auth/kakao/callback");
        params.add("code",code);

        //합치고
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params,headers);

        RestTemplate rt2 = new RestTemplate();

        //보내고 받아서
        ResponseEntity<String> tokenResponse=rt2.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest, // 보낼 데이터
                String.class //
        );

        String accessToken;

        //토큰 추출
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(tokenResponse.getBody());
            accessToken = jsonNode.get("access_token").asText();
            log.info("access_token = {}",accessToken);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return accessToken;

    }

    public String kakaoInfo(String accessToken){

        RestTemplate rt = new RestTemplate();
        //HttpHeaders에 토큰만 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer "+accessToken);

        HttpEntity emailRequest = new HttpEntity(headers);
        ResponseEntity<String> emailResponse=rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                emailRequest, // 보낼 데이터
                String.class //
        );

        ObjectMapper om = new ObjectMapper();
        KakaoTest kakaoTest = new KakaoTest();
        try {
            JsonNode jsonNode = om.readTree(emailResponse.getBody());
            kakaoTest.setName(jsonNode.get("properties").get("nickname").asText());
            kakaoTest.setEmail(jsonNode.get("kakao_account").get("email").asText());
            kakaoTest.setId(jsonNode.get("id").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("kakaoTest == {}" , kakaoTest);






        log.info("emailResponse=={}",emailResponse.getBody());
        return emailResponse.getBody();

    }
}
