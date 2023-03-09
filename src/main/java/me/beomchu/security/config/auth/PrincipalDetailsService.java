package me.beomchu.security.config.auth;

import lombok.RequiredArgsConstructor;
import me.beomchu.security.domain.User;
import me.beomchu.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //throw 걸려있어서 추가로 따로 안함
        User userEntity = userRepository.findByUsername(username);
        return new PrincipalDetails(userEntity);


    }
}
