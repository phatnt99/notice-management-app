package com.phatnt15.noticemanagement.services.impls;

import com.phatnt15.noticemanagement.dtos.AuthUserDetail;
import com.phatnt15.noticemanagement.entities.User;
import com.phatnt15.noticemanagement.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The type User details service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("username %s not found", username)));

        return new AuthUserDetail(user.getId(), user.getUsername(), user.getPassword());
    }
}
