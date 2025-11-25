package com.example.buchladen.Model;


import com.example.buchladen.Enums.OrderStatus;
import com.example.buchladen.Enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "user_id", nullable = false)
    private User user;

    private LocalDateTime orderDate = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.PERSIST)
    private ShippingDetails shippingDetails;


    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(Book book, int quantity) {
        OrderItem item = new OrderItem(this, book, quantity);
        items.add(item);
    }

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean paid;
    private boolean returned;


}
