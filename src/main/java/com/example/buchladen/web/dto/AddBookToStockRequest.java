package com.example.buchladen.web.dto;


import com.example.buchladen.Model.Author;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddBookToStockRequest {
    private String title;
    private String authorName;
    private BigDecimal price;
}
