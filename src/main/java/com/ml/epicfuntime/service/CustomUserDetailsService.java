package com.ml.epicfuntime.service;

import com.ml.epicfuntime.model.Role;
import com.ml.epicfuntime.model.SecurityUser;
import com.ml.epicfuntime.model.User;
import com.ml.epicfuntime.repository.RoleRepository;
import com.ml.epicfuntime.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Role role = roleRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new SecurityUser(user, role);
    }
}
