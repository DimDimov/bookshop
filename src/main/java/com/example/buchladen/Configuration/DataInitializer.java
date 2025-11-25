package com.example.buchladen.Configuration;

import com.example.buchladen.Model.Role;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.RoleRepository;
import com.example.buchladen.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run (String ... args) {
        if (roleRepository.findByName("ROLE_USER") == null) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        if (userRepository.findByEmailOrCustomUsername("youremail@gmail.com", "admin").isEmpty()) {

            User admin  = new User();
            admin.setEmail("youremail@gmail.com");
            admin.setPassword(passwordEncoder.encode("yourpassword"));
            admin.setCustomUsername("admin");
            admin.setUseEmailAsUsername(false);

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            admin.setRoles(Collections.singleton(adminRole));

            userRepository.save(admin);
            System.out.println("admin has been saved.");
        }

    }
}
