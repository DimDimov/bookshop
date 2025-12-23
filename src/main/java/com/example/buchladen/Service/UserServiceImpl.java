package com.example.buchladen.Service;

import com.example.buchladen.Model.Role;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.RoleRepository;
import com.example.buchladen.Repositories.UserRepository;
import com.example.buchladen.web.dto.UserRegistrationDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByLoginIdentifier(String login) {
        return userRepository.findByEmailOrCustomUsername(login, login).orElse(null);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));
    }

    @Override
    public User findByCustomUsername(String username) {
        return userRepository.findByCustomUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(@Valid UserRegistrationDto userDto) {
        User user = new User();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthday(userDto.getBirthday());
        user.setStreet(userDto.getStreet());
        user.setHouseNumber(userDto.getHouseNumber());
        user.setPostcode(userDto.getPostcode());
        user.setTown(userDto.getTown());
        user.setCountry(userDto.getCountry());
        user.setCustomUsername(userDto.getCustomUsername());
        user.setUseEmailAsUsername(userDto.getUseEmailAsUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            throw new RuntimeException("ROLE_USER nicht gefunden.");
        }
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }


    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmailOrCustomUsername(email, email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden: " + email));
    }

    @Override
    public Optional<User> findByEmailOrCustomUsername(String email, String username) {
        return userRepository.findByEmailOrCustomUsername(email, username);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void save(UserRegistrationDto userRegistrationDto) {
    }

    @Override
    @Transactional
    public void enabledUserActive (Long userId, boolean enable) {
        Optional <User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setEnabled(enable);
            userRepository.save(user);
        }
    }

    @Override
    public void update(UserRegistrationDto dto, Authentication authentication) {
        String loginInput = authentication.getName();

       Optional<User> optionalUser = userRepository.findByEmailOrCustomUsername(loginInput, loginInput);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

           user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setStreet(dto.getStreet());
            user.setTown(dto.getTown());
            user.setPostcode(dto.getPostcode());
            user.setCountry(dto.getCountry());

            userRepository.save(user);
        }
    }

}
