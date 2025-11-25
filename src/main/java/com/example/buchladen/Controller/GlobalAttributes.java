package com.example.buchladen.Controller;


import com.example.buchladen.Mapper.UserMapper;
import com.example.buchladen.Model.User;
import com.example.buchladen.Service.*;
import com.example.buchladen.web.dto.UserDto;
import jakarta.annotation.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Objects;

@ControllerAdvice
public class GlobalAttributes {

    private final UserMapper userMapper;
    private final UserService userService;
    private final CartService cartService;
    private final CustomerRequestService customerRequestService;

    public GlobalAttributes( UserMapper userMapper,  UserService userService, CartService cartService, CustomerRequestService customerRequestService) {

        this.userMapper = userMapper;
        this.userService = userService;
        this.cartService = cartService;
        this.customerRequestService = customerRequestService;
    }


    @ModelAttribute("total")
    public long getBooksInCart(Authentication authentication) {

        if (authentication != null) {
            String login = authentication.getName();
            User user = userService.findByEmailOrCustomUsername(login, login).
                    orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

            return cartService.getTotalQuantityByUser(user);
        } else return 0;
    }

    @ModelAttribute("vorname")

    public String getFirstName(@Nullable Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Gast";
        }
        String login = authentication.getName();
        User user = userService.findByEmailOrCustomUsername(login, login).
                orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
        return user.getFirstName();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin (Authentication authentication) {

        if(authentication != null) {
                String login = authentication.getName();
                User user = userService.findByEmailOrCustomUsername(login, login).
                        orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
            return user.getRoles().stream().anyMatch(role -> role
                    .getName().equals("ROLE_ADMIN"));

        }
      return false;
    }

    @ModelAttribute("unreadCount")
    public long getUnreadMessages (Authentication authentication) {
         if (authentication == null) return 0;

         User user = userService.findByEmailOrCustomUsername(authentication.getName(), authentication.getName())
                 .orElseThrow();

        return customerRequestService.findByCustomer(user).stream()
                .flatMap(r -> r.getMessages().stream())
                .filter(msg -> !msg.isReadByUser() && msg.getSender() != null
                        && !Objects.equals(msg.getSender().getId(), user.getId())
                        && !user.equals(msg.getSender()))
                .count();
    }

    @ModelAttribute("user")
    public UserDto getUser(@Nullable Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String login = authentication.getName();
        User user = userService.findByEmailOrCustomUsername(login, login).
                orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
        return userMapper.toDto(user);
    }


}
