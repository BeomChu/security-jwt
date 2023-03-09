package me.beomchu.security.controller;

import lombok.RequiredArgsConstructor;
import me.beomchu.security.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/kakao/callback")
    public @ResponseBody String kakaoCallback(String code){

        String accessToken = authService.kakaoSignup(code);



        return authService.kakaoInfo(accessToken);

    }
}
