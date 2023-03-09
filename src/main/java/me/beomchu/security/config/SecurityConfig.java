package me.beomchu.security.config;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.beomchu.security.config.auth.PrincipalOauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                //authorize
                .authorizeRequests()
                .antMatchers("/user/**")
                .authenticated()
                .antMatchers("/manager/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                //login
                .and()
                .formLogin()
                .loginPage("/login") //GET
                .loginProcessingUrl("/login") //POST
                .defaultSuccessUrl("/") //로그인 성공시 보여줄 디폴트 경ㄹh
                // oauth
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
    }
}
