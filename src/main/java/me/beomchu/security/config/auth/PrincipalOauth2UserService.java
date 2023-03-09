package me.beomchu.security.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.beomchu.security.domain.User;
import me.beomchu.security.oauth.provider.FacebookUserInfo;
import me.beomchu.security.oauth.provider.GoogleUserInfo;
import me.beomchu.security.oauth.provider.Oauth2Info;
import me.beomchu.security.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Configuration
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("userRequest == {}", userRequest);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("user.loadUser(userRequest)== {}", super.loadUser(userRequest).getAttributes());

        Oauth2Info oauth2Info = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            oauth2Info = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            oauth2Info = new FacebookUserInfo(oAuth2User.getAttributes());
        }

        String provider = oauth2Info.getProvider();
        String providerId = oauth2Info.getProviderId();
        String username = provider+ "_"+ providerId;
        String password = provider+ "_"+ "임시비밀번호";
        String encodedPW = encoder().encode(password);
        String email = oauth2Info.getEmail();

        log.info("provider == {}" , provider);

        User userEntity = userRepository.findByUsername(username);

        System.out.println("user Entity == ? {}"+userEntity == null);

        if (userEntity == null) {
           userEntity = User.builder()
                   .username(username)
                   .password(encodedPW)
                   .email(email)
                   .provider(provider)
                   .providerId(providerId)
                   .build();
            System.out.println(userEntity.getProvider());

           userRepository.save(userEntity);
        }

        log.info("userEntity == {}", userEntity);


        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
