package com.realestate.backendrealestate.security.config;


import com.realestate.backendrealestate.core.enums.Role;
import com.realestate.backendrealestate.repositories.UserRepository;
import com.realestate.backendrealestate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.realestate.backendrealestate.entities.User myUser = userService.getUserByEmail(email);
        return new User(myUser.getEmail(), myUser.getPassword(), myUser.getActivated(),
                true, true, true, getAuthorities(myUser.getRole()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.toString()));
}
}