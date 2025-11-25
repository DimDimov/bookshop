package com.example.buchladen.Service;

import com.example.buchladen.Model.ReturnedBook;

import java.util.List;

public interface ReturnedBookService {

    void save(ReturnedBook books);

    List<ReturnedBook> findAll();

    void deleteReturnedBookById(Long id);
}
