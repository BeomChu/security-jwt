package me.beomchu.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.beomchu.security.config.auth.PrincipalDetails;
import me.beomchu.security.dto.request.SignupDto;
import me.beomchu.security.service.AuthService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final AuthService authService;

    @GetMapping("/")
    public @ResponseBody String index(){
         return "인덱스페이지다";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "매니저페이지다";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "어드민페이지다";
    }

    @GetMapping("/login")
    public String login(){
        return "/loginForm";
    }

    @GetMapping("/signup")
    public String signupForm(){
        return "/signupForm";
    }

    @PostMapping("/signup")
    public String signup(SignupDto signupDto){

        authService.signup(signupDto);

        return "redirect:/login";
    }

}
