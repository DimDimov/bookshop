package com.example.buchladen.Repositories;

import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByBook(Book book);
}
