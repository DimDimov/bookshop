package com.example.buchladen.Model;


import com.example.buchladen.Enums.StockStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue
    private Long Id;

    @OneToOne
    private Book book;

    private int quantity;

@Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    private BigDecimal oneBookPrice;
    private BigDecimal totalPrice;
    private LocalDateTime lastUpdate;


    @PrePersist
    @PreUpdate
    public void calculateTotalPrice() {
        if (book != null) {
            if(oneBookPrice == null || quantity == 0) {
                this.totalPrice = BigDecimal.ZERO;
                lastUpdate = LocalDateTime.now();
            } else {
                totalPrice = oneBookPrice.multiply(BigDecimal.valueOf(quantity));
                lastUpdate = LocalDateTime.now();
            }
           /* if (lastUpdate == null) {
                lastUpdate = LocalDateTime.now();
            }*/
        }
    }

}
