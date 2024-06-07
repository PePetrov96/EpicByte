package com.project.EpicByte.service.impl;

import com.project.EpicByte.model.entity.UserEntity;
import com.project.EpicByte.model.entity.UserRoleEntity;
import com.project.EpicByte.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserServiceDetails implements UserDetailsService {
    private final UserRepository userRepository;

    public MyUserServiceDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findUserByUsername(username)
                .map(userEntity -> {
                    System.out.println("User: " + userEntity.getUsername() + " has roles: " + userEntity.getRoles());
                    return map(userEntity);
                })
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
    }

    protected UserDetails map(UserEntity userEntity) {
        return User
                .withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(userEntity
                        .getRoles()
                        .stream()
                        .map(MyUserServiceDetails::map)
                        .toList())
                .build();
    }

    private static GrantedAuthority map(UserRoleEntity userRoleEntity) {
        return new SimpleGrantedAuthority("ROLE_" + userRoleEntity.getRole());
    }
}
