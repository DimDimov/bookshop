package com.example.buchladen.Mapper;

import com.example.buchladen.Model.ShippingDetails;
import com.example.buchladen.web.dto.DeliveryDto;

public class DeliveryMapper {


   public DeliveryDto shippingToDto(ShippingDetails shipping) {

        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setFirstName(shipping.getFirstName());
        deliveryDto.setLastName(shipping.getLastName());
        deliveryDto.setTown(shipping.getTown());
        deliveryDto.setPostcode(shipping.getPostcode());
        deliveryDto.setHouseNumber(shipping.getHouseNumber());
        deliveryDto.setCountry(shipping.getCountry());
        return deliveryDto;
    }
}
