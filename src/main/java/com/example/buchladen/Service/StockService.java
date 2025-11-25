package com.example.buchladen.Service;

import com.example.buchladen.Model.Author;
import com.example.buchladen.Model.Stock;
import com.example.buchladen.web.dto.AddBookToStockRequest;

import java.util.List;

public interface StockService {

    void deleteBookFromStock(Long id);

    Stock addNewBookToStock(AddBookToStockRequest request);

    List<Stock> findAll();

    Author findByName(String name);

    void save(Stock stock);

    Stock findById(Long id);

    double calculateCostFullStock();
}
