package com.example.buchladen.Service;

import com.example.buchladen.Model.ShippingDetails;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.ShippingRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShippingServiceImpl implements ShippingService {

    private final ShippingRepository shippingRepository;

    public ShippingServiceImpl(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    @Override
    public void save(ShippingDetails shippingDetails) {
        shippingRepository.save(shippingDetails);
    }

    @Override
    public ShippingDetails findById(Long id) {
        return shippingRepository.findById(id).orElseThrow(() -> new RuntimeException("Falsch delivery ID" + id));
    }

    @Override
    public List<ShippingDetails> findByUser(User user) {
        return shippingRepository.findByUser(user);
    }

    @Override
    public void delete(ShippingDetails shippingDetails) {
        shippingRepository.delete(shippingDetails);
    }

    @Override
    public void saveAll(List<ShippingDetails> shippingDetails){
        shippingRepository.saveAll(shippingDetails);
    }
}
