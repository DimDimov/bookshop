package com.example.buchladen.Mapper;

import com.example.buchladen.Model.User;
import com.example.buchladen.web.dto.UserDto;
import com.example.buchladen.web.dto.UserRegistrationDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getCustomUsername(), user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getUseEmailAsUsername(), user.getTown(), user.getStreet(),
                user.getCountry(),  user.getHouseNumber(),  user.getPostcode(), user.isEnabled(), user.getBirthday(), user.getPassword());
    }


    public UserRegistrationDto toRegistrationDto(User user) {
        return new UserRegistrationDto(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(),
                user.getBirthday(), user.getStreet(), user.getHouseNumber(),
                user.getPostcode(), user.getTown(), user.getCountry(), user.getResetPasswordToken(), user.getUseEmailAsUsername(), user.getCustomUsername());
    }

    public User toEntity(UserDto userDto) {

        User user = new User();

        user.setFirstName( userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthday(userDto.getBirthday());
        user.setStreet( userDto.getStreet());
        user.setHouseNumber(userDto.getHouseNumber());
        user.setPostcode( userDto.getPostcode());
        user.setTown(userDto.getTown());
        user.setCountry(userDto.getCountry());
        user.setCustomUsername(userDto.getCustomUsername());
        user.setUseEmailAsUsername(true);
        user.setEnabled(true);
        user.setEmail(user.getEmail());
        return user;
    }
}
