package com.example.buchladen.Controller;


import com.example.buchladen.Model.ShippingDetails;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.ShippingRepository;
import com.example.buchladen.Repositories.UserRepository;
import com.example.buchladen.Service.ShippingService;
import com.example.buchladen.Service.UserService;
import com.example.buchladen.Service.UserServiceImpl;
import com.example.buchladen.web.dto.UserRegistrationDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class FormController implements WebMvcConfigurer {

    private final UserService userService;

    private final ShippingService shippingService;

    public FormController(UserService userService, ShippingService shippingService) {
        this.userService = userService;
        this.shippingService = shippingService;
    }

    @GetMapping("/user_form")
    public String showRegistrationForm(Model model) {

        UserRegistrationDto userDto = new UserRegistrationDto();

        userDto.setUseEmailAsUsername(true);

        model.addAttribute("registration_form", userDto);
        return "user_form";
    }

    @RequestMapping(value="/user_form", method = RequestMethod.POST)
    public String signup(@Valid @ModelAttribute("registration_form") UserRegistrationDto userDto,
                         BindingResult result,
                         Model model) {

        if(result.hasErrors()) {
            model.addAttribute("user", userDto);
        }

        Boolean useEmailAsUsername = userDto.getUseEmailAsUsername();

        if(Boolean.TRUE.equals(useEmailAsUsername)) {
            userDto.setCustomUsername(userDto.getEmail());
        }
        else {
            userDto.setCustomUsername(userDto.getCustomUsername());
        }

        if(userService.existByEmail(userDto.getEmail())) {
            model.addAttribute("warning","Email ist bereits vergeben!");
            return "user_form";
        }


        userService. createUser(userDto);
       User savedUser = userService.findByEmailOrCustomUsername(userDto.getEmail(), userDto.getEmail())
               .orElseThrow();

        ShippingDetails shipping = new ShippingDetails();
        shipping.setFirstName(userDto.getFirstName());
        shipping.setLastName(userDto.getLastName());
        shipping.setStreet(userDto.getStreet());
        shipping.setTown(userDto.getTown());
        shipping.setHouseNumber(userDto.getHouseNumber());
        shipping.setCountry(userDto.getCountry());
        shipping.setPostcode(userDto.getPostcode());
        shipping.setDefault(true);
        shipping.setUser(savedUser);

        shippingService.save(shipping);

        model.addAttribute("message1", "Sie haben sich erfolgreich angemeldet!");
        model.addAttribute("message2",   "Sie werden in ");
        model.addAttribute("message3", " Sekunden zur Anmeldeseite weitergeleitet." );
        model.addAttribute("message4",  "Alternativ können Sie auch auf die Schaltfläche unten klicken.");

        return "user_form";
    }
}
