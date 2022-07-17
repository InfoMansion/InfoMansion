package com.infomansion.server.global.util.security;

import com.infomansion.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + ": 데이터베이스에서 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(com.infomansion.server.domain.user.domain.User myUser) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(myUser.getAuthority().toString());

        return new User(
                String.valueOf(myUser.getId()),
                myUser.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
