package com.example.buchladen.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private Long bookId;
    private String title;
    private String author;
    private String imageBook;
    private BigDecimal price;
    private int quantity;
    private BigDecimal itemTotalPrice;
    private String ImageURL;

   /* public CartItemDto(Long id, String title, String author, String imageBook, double price, Integer stockQuantity) {
    }*/

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }


}
