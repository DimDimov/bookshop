package com.example.buchladen.Repositories;

import com.example.buchladen.Model.CartItem;
import com.example.buchladen.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemsRepository extends JpaRepository<CartItem, Long> {

    List<CartItem>findItemByUser(User user);
}
