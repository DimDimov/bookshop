package com.example.buchladen.Repositories;

import com.example.buchladen.Enums.RequestStatus;
import com.example.buchladen.Model.CustomerRequest;
import com.example.buchladen.Model.Order;
import com.example.buchladen.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequest, Long> {

    List<CustomerRequest> findAllByOrderByCreatedAtDesc();

    List<CustomerRequest> findByCustomer(User user);

    Optional<CustomerRequest> findByCustomerAndOrder(User user, Order order);

}
