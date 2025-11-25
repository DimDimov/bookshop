package com.example.buchladen.Repositories;

import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.OrderItem;
import com.example.buchladen.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByBook(Book book);

    List<OrderItem> findByOrder_User(User user);

    List<OrderItem> findByOrder_UserAndBook(User user, Book book);
}
