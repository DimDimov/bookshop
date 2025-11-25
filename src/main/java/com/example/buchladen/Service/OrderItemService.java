package com.example.buchladen.Service;

import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.OrderItem;
import com.example.buchladen.Model.User;

import java.util.List;

public interface OrderItemService {


    List<OrderItem> findByOrder_UserAndBook(User user, Book book);

    List<OrderItem> findByBook(Book book);

    List<OrderItem> findByOrder_User(User user);

    List<OrderItem> findAll();

    OrderItem findById(Long id);

    void save(OrderItem item);
}
