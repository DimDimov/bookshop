package com.example.buchladen.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Order order;

    @ManyToOne
    private OrderItem item;

    @ManyToOne
    private User user;

    private int quantity;

    private BigDecimal refundAmount;

    private LocalDateTime returnedDate;
}
