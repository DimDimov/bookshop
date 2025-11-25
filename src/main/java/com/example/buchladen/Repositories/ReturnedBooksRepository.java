package com.example.buchladen.Repositories;

import com.example.buchladen.Model.ReturnedBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnedBooksRepository extends JpaRepository<ReturnedBook, Long> {


    void deleteReturnedBookById(Long id);

}
