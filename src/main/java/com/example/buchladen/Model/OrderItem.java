package com.example.buchladen.Model;


import com.example.buchladen.Enums.PaymentMethod;
import com.example.buchladen.web.dto.OrderDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    private Stock stock;

    private double quantity;

    private BigDecimal price;

    private PaymentMethod paymentMethod;

    private boolean returned = false;

    public OrderItem(Order order, Book book, int quantity) {
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.price = book.getPrice();
    }

    public OrderItem(OrderDto orderDto, Book book, int quantity) {
    }
}
