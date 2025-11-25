package com.example.buchladen.Service;

import com.example.buchladen.Details.CustomUserDetails;
import com.example.buchladen.Model.Role;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.UserRepository;
import com.example.buchladen.web.dto.UserRegistrationDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final  UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByEmailOrCustomUsername(login, login);

        if(optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");

        }
        User user = optionalUser.get();

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new  org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), user.isEnabled(), true, true, true, authorities
        );

    }

    public void updateResetPasswordToken(String token, String login) throws UsernameNotFoundException {
        Optional<User> optionalUser  = userRepository.findByEmailOrCustomUsername(login, login);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setResetPasswordToken(token);
            userRepository.save(user);
        }
        else {
            throw new UsernameNotFoundException("Es konnte kein Benutzer mit der E-Mail gefunden werden");
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword (User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    public Collection<? extends GrantedAuthority> authorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }


}
