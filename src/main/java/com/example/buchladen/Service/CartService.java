package com.example.buchladen.Service;

import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.Cart;
import com.example.buchladen.Model.CartItem;
import com.example.buchladen.Model.User;
import com.example.buchladen.web.dto.CartDto;
import com.example.buchladen.web.dto.CartItemDto;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

public interface CartService {

    Cart getCartForUser(User user);

    void addBookToCart(User user, Book book, int quantity);

    void removeBookFromCart( User user, Long bookId);

    void clearCart(User user);

    int getTotalQuantityByUser(User user);

    CartDto convertToCartDto(Cart cart);

    int getQuantityForBook(User user, Long bookId);

    int updateBooksQuantity(User user, Long bookId, int changeQuantity);

   List<CartItem> findItemByUser(User user);

   Optional<Cart> findByUserId(Long id);

}
