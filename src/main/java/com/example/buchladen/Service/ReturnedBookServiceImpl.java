package com.example.buchladen.Service;


import com.example.buchladen.Model.ReturnedBook;
import com.example.buchladen.Repositories.ReturnedBooksRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnedBookServiceImpl implements ReturnedBookService {

    private final ReturnedBooksRepository returnedBooksRepository;

    public ReturnedBookServiceImpl(ReturnedBooksRepository returnBooksRepository) {
        this.returnedBooksRepository = returnBooksRepository;
    }

    @Override
    public void save(ReturnedBook books) {
        returnedBooksRepository.save(books);
    }

    @Override
    public List<ReturnedBook> findAll() {
        return returnedBooksRepository.findAll();
    }

    @Override
    public void deleteReturnedBookById(Long id) {
        returnedBooksRepository.deleteReturnedBookById(id);
    }



}
