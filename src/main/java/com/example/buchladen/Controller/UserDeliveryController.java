package com.example.buchladen.Controller;

import com.example.buchladen.Mapper.UserMapper;
import com.example.buchladen.Model.ShippingDetails;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.*;
import com.example.buchladen.Service.OrderService;
import com.example.buchladen.Service.ShippingService;
import com.example.buchladen.Service.UserService;
import com.example.buchladen.web.dto.ShippingRegistrationDto;
import com.example.buchladen.web.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("delivery")
public class UserDeliveryController {

    private final UserService userService;
    private  final UserMapper userMapper;
    private final ShippingService shippingService;
    private final OrderService orderService;

    public UserDeliveryController( UserService userService, UserMapper userMapper, ShippingService shippingService, OrderService orderService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.shippingService = shippingService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/get_delivery")
    public String getDelivery (Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String login = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));
        List<ShippingDetails> allAddresses = shippingService.findByUser(user);
        UserDto userDto = userMapper.toDto(user);

        ShippingDetails defaultAddress = allAddresses.stream()
                .filter(ShippingDetails::isDefault)
                .findFirst()
                .orElse(null);

        List<ShippingDetails> otherAddresses = allAddresses.stream()
                .filter(addr -> !addr.isDefault())
                .toList();

        model.addAttribute("user", userDto);// for test
        model.addAttribute("defaultAddress", defaultAddress);
        model.addAttribute("otherAddresses", otherAddresses);

        return "delivery";
    }

    @GetMapping(value= "/add_delivery_address")
    public String addDeliveryAddress(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        model.addAttribute("shipping", new ShippingDetails());
        return "addDeliveryAddress";
    }

    @RequestMapping(value="/add_delivery_address", method = RequestMethod.POST)
    public String addDeliveryAddress (@Valid @ModelAttribute("shipping") ShippingRegistrationDto shippingDto, BindingResult result, Authentication authentication,
                                      Model model ) {

        if (authentication == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("shipping", shippingDto);
            return "/addDeliveryAddress";
        }

        String login = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));

        ShippingDetails  shipping = new ShippingDetails();
        shipping.setFirstName(shippingDto.getFirstName());
        shipping.setLastName(shippingDto.getLastName());
        shipping.setCountry(shippingDto.getCountry());
        shipping.setHouseNumber(shippingDto.getHouseNumber());
        shipping.setStreet(shippingDto.getStreet());
        shipping.setTown(shippingDto.getTown());
        shipping.setPostcode(shippingDto.getPostcode());
        shipping.setUser(user);
        user.getShippingDetailsList().add(shipping);
        userService.save(user);

        return "redirect:/delivery/get_delivery";
    }

    @GetMapping("/edit_delivery/{id}")
    public String editDeliveryAddress(@PathVariable Long id, Model model) {
        ShippingDetails delivery = shippingService.findById(id);
        model.addAttribute("delivery", delivery);
        return "editDeliveryInfo";
    }

    @PostMapping("/edit_delivery")
    public String updateDelivery(@Valid @ModelAttribute ("delivery") ShippingRegistrationDto shippingDto,
                                 BindingResult result, Authentication authentication,
                                 Model model) {

        if(result.hasErrors()) {
            model.addAttribute("delivery", shippingDto);
            return "editDeliveryInfo";
        }

        User user = userService.findByEmail(authentication.getName());

        ShippingDetails existing = shippingService.findById(shippingDto.getId());

        existing.setFirstName(shippingDto.getFirstName());
        existing.setLastName(shippingDto.getLastName());
        existing.setStreet(shippingDto.getStreet());
        existing.setHouseNumber(shippingDto.getHouseNumber());
        existing.setPostcode(shippingDto.getPostcode());
        existing.setTown(shippingDto.getTown());
        existing.setCountry(shippingDto.getCountry());

        existing.setUser(user);
        shippingService.save( existing);
        return "redirect:/delivery/get_delivery";
    }

    @DeleteMapping ("/delete_delivery/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteDelivery(@PathVariable long id, Authentication authentication) {

        String login = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));
        ShippingDetails details= shippingService.findById(id);

        assert details != null;
        if (!Objects.equals(details.getUser().getId(), user.getId()) || details.isDefault()) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if(orderService.existsByShippingDetails(details)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Adresse wird in einer Bestellung vewendet und kann nicht geloescht werden!");
        }

        shippingService.delete(details);
        return ResponseEntity.ok().build();
      /*  return ResponseEntity.noContent().build();  // for test*/

    }

    @PostMapping("/make_default/{id}")
    public String makeDefaultAddress(@PathVariable Long id, Authentication authentication) {

        String login = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));
        List<ShippingDetails> allAddresses = shippingService.findByUser(user);

        for(ShippingDetails address : allAddresses) {
            address.setDefault(false);
        }
        ShippingDetails selected = shippingService.findById(id);

        if (!Objects.equals(selected.getUser().getId(), user.getId())) {
            throw  new SecurityException("Access denied to modify another user's address.");
        }
        selected.setDefault(true);
        shippingService.saveAll(allAddresses);
        return "redirect:/my_account/get_delivery";
    }

}
