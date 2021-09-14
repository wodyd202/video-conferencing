package com.ljy.videoclass.user.query.application;

import com.ljy.videoclass.user.domain.model.UserModel;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserSearchService implements UserDetailsService {
    private final UserQueryRepository userQueryRepository;

    public UserSearchService(UserQueryRepository userQueryRepository) {
        this.userQueryRepository = userQueryRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserModel userModel = userQueryRepository.findLoginInfoByUserId(userId).orElseThrow(() -> new UsernameNotFoundException(userId));
        return new User(userModel.getUserId(),userModel.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
