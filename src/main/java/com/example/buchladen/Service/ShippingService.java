package com.example.buchladen.Service;

import com.example.buchladen.Model.ShippingDetails;
import com.example.buchladen.Model.User;

import java.util.List;

public interface ShippingService {


    void save(ShippingDetails shippingDetails);

    ShippingDetails findById(Long id);

    List<ShippingDetails> findByUser(User user);

    void delete (ShippingDetails shippingDetails);

    void saveAll(List<ShippingDetails> shippingDetails);

}
