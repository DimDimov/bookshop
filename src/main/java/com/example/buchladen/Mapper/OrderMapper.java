package com.example.buchladen.Mapper;


import com.example.buchladen.Model.Order;
import com.example.buchladen.web.dto.OrderDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

   public OrderDto toOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setPaymentMethod(order.getPaymentMethod());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setTotalPrice(order.getTotalAmount());
        orderDto.setStatus(order.getStatus());
       return orderDto;
    }
}
