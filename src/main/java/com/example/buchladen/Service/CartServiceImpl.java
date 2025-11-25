
package com.example.buchladen.Service;

import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.Cart;
import com.example.buchladen.Model.CartItem;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.BookRepository;
import com.example.buchladen.Repositories.CartItemsRepository;
import com.example.buchladen.Repositories.CartRepository;
import com.example.buchladen.Repositories.UserRepository;
import com.example.buchladen.web.dto.CartDto;
import com.example.buchladen.web.dto.CartItemDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;

    private final CartItemsRepository cartItemsRepository;


  public CartServiceImpl(CartRepository cartRepository, CartItemsRepository cartItemsRepository) {
      this.cartRepository = cartRepository;
      this.cartItemsRepository = cartItemsRepository;
  }

    @Override
    public Cart getCartForUser(User user) {
        return cartRepository.findByUserId(user.getId()).orElseGet
                (() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setCartItems(new ArrayList<>());
                    return cartRepository.save(cart);
                        }
                );
    }
@Override
    public int getTotalQuantityByUser(User user) {
    return cartRepository.findByUserId(user.getId()).map(cart -> cart.getCartItems().stream()
            .mapToInt(CartItem::getQuantity).sum()).orElse(0);
    }

    @Override
    public Optional<Cart> findByUserId(Long id) {
      return cartRepository.findByUserId(id);
    }

    @Override
    @Transactional
    public void addBookToCart(User user, Book book, int quantity) {

      Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);

      if(cart == null) {
          cart = new Cart();
          cart.setUser(user);
          cart = cartRepository.save(cart);
      }

        Optional< CartItem> existingItem = cart.getCartItems().stream().filter(item -> item.getBook().getId().equals(book.getId()) ).findFirst();

        if(existingItem.isPresent()) {

            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);

            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);

        }
        cartRepository.save(cart);
    }

    @Override
    public void removeBookFromCart(User user, Long bookId) {
       Cart cart = getCartForUser(user);

       cart.getCartItems().removeIf(item -> item.getBook().getId().equals(bookId));

       cartRepository.save(cart);
    }

    @Override
    public void clearCart (User user) {
       Cart cart = getCartForUser(user);
       cart.getCartItems().clear();
       cartRepository.save(cart);
    }

    @Override
    public CartDto convertToCartDto (Cart cart) {

      CartDto cartDto = new CartDto();

        List <CartItemDto> itemsDto =
                cart.getCartItems().stream().map(item -> {
                    CartItemDto cartItemDto = new CartItemDto();

                    BigDecimal price = item.getBook().getPrice();

                    int quantity = item.getQuantity();

                    BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));

                    cartItemDto.setBookId(item.getBook().getId());
                    cartItemDto.setTitle(item.getBook().getTitle());
                    cartItemDto.setPrice(item.getBook().getPrice());
                    cartItemDto.setQuantity(item.getQuantity());
                    cartItemDto.setImageURL(item.getBook().getImageBook());
                    cartItemDto.setAuthor(item.getBook().getAuthor().getName());
                    cartItemDto.setItemTotalPrice(totalPrice);

                  return cartItemDto;
                }).collect(Collectors. toList());

        BigDecimal totalCartPrice = itemsDto.stream().map(CartItemDto::getItemTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cartDto.setCartItemDto(itemsDto);
        cartDto.setTotalPrice(totalCartPrice);

        return  cartDto;
    }

    @Override
    public int updateBooksQuantity(User user, Long bookId, int changeQuantity) {
      Cart cart = cartRepository.findByUserId(user.getId())
              .orElseThrow(() -> new RuntimeException("Korb nicht gefunden"));

      Optional<CartItem> itemOpt = cart.getCartItems().stream()
                      .filter(i -> i.getBook().getId().equals(bookId)).findFirst();

      if(itemOpt.isPresent()) {
          CartItem item = itemOpt.get();

          int newQuantity = item.getQuantity() + changeQuantity;

          if (newQuantity <= 0) {
              cart.getCartItems().remove(item);
              cartItemsRepository.delete(item);
          } else {
              item.setQuantity(newQuantity);
          }
          cartRepository.save(cart);
          return Math.max(newQuantity, 0);
      } else {
          throw new RuntimeException("Buch nicht gefunden");
      }
    }

 @Override
    public int getQuantityForBook(User user, Long bookId) {

      Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(()
              -> new RuntimeException("Warenkorb nicht gefunden."));
      return cart.getCartItems().stream().filter(item -> item.getBook()
              .getId().equals(bookId)).mapToInt(CartItem::getQuantity).findFirst().orElse(0);
 }

 @Override
 public List<CartItem> findItemByUser(User user) {
      return  cartItemsRepository.findItemByUser(user);
 }
}

