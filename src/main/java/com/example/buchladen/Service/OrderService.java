package com.example.buchladen.Service;

import com.example.buchladen.Model.Order;
import com.example.buchladen.Model.ShippingDetails;
import com.example.buchladen.Model.User;

import java.util.List;

public interface OrderService {


    List<Order> findOrdersByUser(User user);

    Order findOrderById(Long id);

    Order createOrUpdateOrder(User user, ShippingDetails details);

    void save(Order order);

    void completeOrder(Order order);

    boolean existsByShippingDetails(ShippingDetails shippingDetails);

    double calculateProfit();
}
