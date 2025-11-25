package com.example.buchladen.web.dto;

import com.example.buchladen.Enums.OrderStatus;
import com.example.buchladen.Enums.PaymentMethod;
import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private BigDecimal totalPrice;
    private UserDto user;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private DeliveryDto deliveryDto;
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(Book book, int quantity) {
        OrderItem item = new OrderItem(this, book, quantity);
        items.add(item);
    }
}
