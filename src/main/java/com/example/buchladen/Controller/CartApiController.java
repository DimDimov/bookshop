package com.example.buchladen.Controller;


import com.example.buchladen.Model.Cart;
import com.example.buchladen.Model.CartItem;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.CartItemsRepository;
import com.example.buchladen.Repositories.CartRepository;
import com.example.buchladen.Repositories.UserRepository;
import com.example.buchladen.Service.CartService;
import com.example.buchladen.Service.CartServiceImpl;
import com.example.buchladen.Service.UserService;
import com.example.buchladen.web.dto.CartDto;
import com.example.buchladen.web.dto.CartItemDto;
import com.example.buchladen.web.dto.UpdateBooksInCart;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartApiController {

    public final UserService userService;
    public final CartItemsRepository cartItemsRepository;
    private final CartService cartService;

    public CartApiController(  UserService userService, CartItemsRepository cartItemsRepository, CartService cartService) {
        this.userService = userService;
        this.cartItemsRepository = cartItemsRepository;
        this.cartService = cartService;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCart(@RequestBody UpdateBooksInCart updateBooksDto,

    Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        String login = authentication.getName();
        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow(()-> new RuntimeException("Benutzer nicht gefunden."));


        Optional<Cart> optionalCart = cartService.findByUserId(user.getId());

        if(optionalCart.isEmpty()){
            return ResponseEntity.ok( Map.of("totalPrice", 0));
        }

        Cart cart = optionalCart.get();

     CartDto cartDto = cartService.convertToCartDto(cart);

     CartItemDto cartItemDto = cartDto.getCartItemDto().stream()
             .filter(i -> i.getBookId().equals(updateBooksDto.getBookId()))
             .findFirst().orElse(null);

        try{

      int newQuantity = cartService.updateBooksQuantity(user, updateBooksDto.getBookId(),
                 updateBooksDto.getChangeQuantity());

        int totalQuantity = cartService.getTotalQuantityByUser(user);

            BigDecimal itemTotalPrice = (cartItemDto != null) ?
                    cartItemDto.getItemTotalPrice() : BigDecimal.ZERO;

            cartDto = cartService.convertToCartDto(cart);

            BigDecimal totalCartPrice = cartDto.getTotalPrice();

            response.put("success", true);
            response.put("newQuantity", newQuantity);
            response.put("totalQuantity", totalQuantity);
            response.put("itemTotalPrice", itemTotalPrice);
            response.put("totalCartPrice", totalCartPrice);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        }
    }

    @PostMapping("/total")
    @ResponseBody
    public ResponseEntity<  Map<String, Object>> getCartTotal (Authentication authentication) {

        Map<String,Object> response = new HashMap<>();

        String login = authentication.getName();
        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow(()-> new RuntimeException("Benutzer nicht gefunden."));


        Optional<Cart> optionalCart = cartService.findByUserId(user.getId());

        if(optionalCart.isEmpty()){
            return ResponseEntity.ok(  Map.of("totalQuantity", 0));

        }
        Cart cart = optionalCart.get();

            int totalQuantity = cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum();

            CartDto cartDto = cartService.convertToCartDto(cart);

            BigDecimal totalCartPrice = cartDto.getTotalPrice();

            response.put("success", true);
            response.put("total", totalQuantity);
            response.put("totalPrice", totalCartPrice);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/delete_item/{id}")
    @ResponseBody
    public ResponseEntity<?>deleteCartItem(@PathVariable Long id, Authentication authentication) {

        String login = authentication.getName();
        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow(()-> new RuntimeException("Benutzer nicht gefunden."));

        cartService.removeBookFromCart(user, id);

        Cart cart = cartService.getCartForUser(user);
        int totalQuantity = cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum();
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> (item.getBook()
                .getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> response = new HashMap<>();

        Optional<Cart>optionalCart = cartService.findByUserId(user.getId());

        if(optionalCart.isEmpty()){
            return ResponseEntity.ok( Map.of("totalPrice", 0));
        }

      Cart cart1 = optionalCart.get();

       CartDto cartDto = cartService.convertToCartDto(cart1);

        BigDecimal totalCartPrice = cartDto.getTotalPrice();



        response.put("success", true);
        response.put("totalQuantity", totalQuantity);
        response.put("totalPrice", totalPrice);
        response.put("totalCartPrice", totalCartPrice);
        return ResponseEntity.ok(response);

    }
}
