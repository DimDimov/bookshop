package com.example.buchladen.Service;

import com.example.buchladen.Enums.RequestStatus;
import com.example.buchladen.Model.CustomerRequest;
import com.example.buchladen.Model.Order;
import com.example.buchladen.Model.User;

import java.util.List;
import java.util.Optional;

public interface CustomerRequestService {

    Optional<CustomerRequest>findByCustomerAndOrder(User user, Order order);

    void save(CustomerRequest request);

    List<CustomerRequest> findByCustomer(User user);

    CustomerRequest findById(Long id);

    void updateStatus(Long id, RequestStatus status);

    void saveAdminMessage(Long id, String answer, User admin);

    List<CustomerRequest> getAllRequests();
}
