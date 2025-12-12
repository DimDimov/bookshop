package com.example.buchladen.Configuration;

import com.example.buchladen.Model.Role;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.RoleRepository;
import com.example.buchladen.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run (String ... args) {
        if (roleRepository.findByName("ROLE_USER") == null) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        if (userRepository.findByEmailOrCustomUsername(adminEmail, "admin").isEmpty()) {

            User admin  = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));

            admin.setCustomUsername("admin");
            admin.setUseEmailAsUsername(false);

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            admin.setRoles(Collections.singleton(adminRole));

            userRepository.save(admin);
            System.out.println("admin has been saved.");
        }

    }
}
