package com.example.buchladen.Service;

import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.OrderItem;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.OrderItemsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService{

    private final OrderItemsRepository orderItemsRepository;

    public OrderItemServiceImpl(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }

    public List<OrderItem> findByOrder_UserAndBook(User user, Book book) {
        return orderItemsRepository.findByOrder_UserAndBook(user, book);
    }

    public List<OrderItem> findByBook(Book book) {
        return orderItemsRepository.findByBook(book);
    }

    public List<OrderItem> findByOrder_User(User user) {
        return orderItemsRepository.findByOrder_User(user);
    }

    public List<OrderItem> findAll() {
        return orderItemsRepository.findAll();
    }

    public OrderItem findById(Long id) {
        return orderItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Waren nicht gefunden."));
    }

    public void save(OrderItem item) {
        orderItemsRepository.save(item);
    }
}
