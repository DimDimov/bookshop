package com.example.buchladen.Repositories;

import com.example.buchladen.Model.ShippingDetails;
import com.example.buchladen.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingRepository extends JpaRepository<ShippingDetails, Long> {
    List<ShippingDetails> findByUser(User user);

}
