package com.ljy.videoclass.services.panelist.query.application;

import com.ljy.videoclass.services.panelist.domain.model.PanelistModel;
import com.ljy.videoclass.services.panelist.query.infrastructure.RedisQueryPanelistRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class QueryPanelistService implements UserDetailsService {
    private RedisQueryPanelistRepository panelistRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        PanelistModel panelistModel = panelistRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));
        return new User(panelistModel.getId(), panelistModel.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROLE_PANELIST")));
    }
}
