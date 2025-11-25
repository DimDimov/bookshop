package com.example.buchladen.web.dto;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDto {

    private List<CartItemDto> cartItemDto;
    private BigDecimal totalPrice;
}
