package com.example.buchladen.Repositories;

import com.example.buchladen.Model.Order;
import com.example.buchladen.Model.ShippingDetails;
import com.example.buchladen.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.status = 'OPEN'")

  Optional <Order> findOpenOrderByUser(@Param("user") User user);

    boolean existsByShippingDetails(ShippingDetails shippingDetails);

    List<Order> findByUser (User user);

}
