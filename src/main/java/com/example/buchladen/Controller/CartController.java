package com.example.buchladen.Controller;


import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.Cart;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.BookRepository;
import com.example.buchladen.Repositories.UserRepository;
import com.example.buchladen.Service.BookService;
import com.example.buchladen.Service.CartService;
import com.example.buchladen.Service.CartServiceImpl;
import com.example.buchladen.Service.UserService;
import com.example.buchladen.web.dto.CartDto;
import com.example.buchladen.web.dto.CartItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
public class CartController {

    private final UserService userService;

    private final CartService cartService;

    private final BookService bookService;

    public CartController(UserService userService, CartService cartService, BookService bookService) {
        this.userService = userService;
        this.cartService = cartService;

        this.bookService = bookService;
    }

    @GetMapping("/cart")
    public String getShoppingTrolley(Model model, Authentication authentication) {

        if(authentication == null) {
            return "redirect:login";
        }

        String login = authentication.getName();

        Optional<User> optionalUser = userService.findByEmailOrCustomUsername(login, login);

        if(optionalUser.isEmpty()) {
            throw  new RuntimeException("Benutzer nicht gefunden");
        }

        User user = optionalUser.get();

        Cart cart = cartService.getCartForUser(user);

        CartDto cartDto = cartService.convertToCartDto(cart);

        boolean isEmpty = cart.getCartItems().isEmpty();

        int total = cartService.getTotalQuantityByUser(user);

        boolean isAdmin = user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));



        if(isAdmin) {
            model.addAttribute("total", 0);
        }


        model.addAttribute("cart", cartDto);
        model.addAttribute("total", total);
        model.addAttribute("cartPrice", cartDto.getTotalPrice());
        model.addAttribute("vorname", user.getFirstName());
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("step", 1);
        model.addAttribute("empty", isEmpty);

        return "cart-page";
    }

    @PostMapping("/cart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> postShoppingTrolley(@RequestBody CartItemDto cartItemDto, Authentication authentication, Model model)
    {
        User user = userService.findByEmail(authentication.getName());

        Book book = bookService.findById(cartItemDto.getBookId());

        cartService.addBookToCart(user, book, cartItemDto.getQuantity());

        int totalQuantity = cartService.getTotalQuantityByUser(user);

        Map<String, Object> response = new HashMap<>();


        response.put("totalQuantity", totalQuantity);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }


}
