package com.example.buchladen.Repositories;

import com.example.buchladen.Model.Cart;
import com.example.buchladen.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);
}
