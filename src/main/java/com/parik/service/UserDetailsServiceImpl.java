package com.parik.service;

import com.parik.model.User;
import com.parik.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Реализация UserDetailsService для Spring Security.
 * Загружает данные пользователя из базы данных и преобразует роли в GrantedAuthority.
 * 
 * @author Курбанов Умар Рашидович
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JpaUserRepository userRepository;

    /**
     * Загружает пользователя по имени пользователя для Spring Security.
     * 
     * @param username имя пользователя (логин)
     * @return объект UserDetails с данными пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPasswordHash(),
            getAuthorities(user.getRole())
        );
    }

    /**
     * Преобразует роль пользователя в формат Spring Security (ROLE_*).
     * 
     * @param role роль пользователя (Администратор, Мастер, Клиент)
     * @return коллекция GrantedAuthority с ролью пользователя
     */
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        String rolePrefix = "ROLE_";
        String roleName = rolePrefix + role.toUpperCase()
            .replace("АДМИНИСТРАТОР", "ADMIN")
            .replace("МАСТЕР", "MASTER")
            .replace("КЛИЕНТ", "CLIENT");
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }
}

