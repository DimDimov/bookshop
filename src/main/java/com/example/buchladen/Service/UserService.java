package com.example.buchladen.Service;

import com.example.buchladen.Model.User;
import com.example.buchladen.web.dto.UserRegistrationDto;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface UserService {

     User findByEmail(String email);

     User findById(Long id);

     User findByCustomUsername(String username);

    Optional<User> findByEmailOrCustomUsername(String email, String username);

    boolean existByEmail(String email);

    void save (@Valid UserRegistrationDto userRegistrationDto);

    void update (@Valid UserRegistrationDto userRegistrationDto, Authentication authentication);

    void save(User user);

   void createUser (@Valid UserRegistrationDto userDto);

   User findByLoginIdentifier(String login);

    List<User> findAllUsers();

    void enabledUserActive(Long userId, boolean enable);

}
