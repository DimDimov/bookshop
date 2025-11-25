package com.example.buchladen.web.dto;

import com.example.buchladen.Enums.StockStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class StockDto {
    private Long id;
    private int quantity;
    private BigDecimal oneBookPrice;
    private BigDecimal totalPrice;
    private StockStatus stockStatus;
    private LocalDateTime lastUpdate;
    private BookDto bookDto;

}
