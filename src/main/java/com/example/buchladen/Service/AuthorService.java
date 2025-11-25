package com.example.buchladen.Service;

import com.example.buchladen.Model.Author;
import com.example.buchladen.Model.Stock;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AuthorService {

    List<Author> findAll();

    void deleteAuthor(Long id);

    String saveImage(MultipartFile file) throws IOException;

    void save(Author author);

    Author findById(Long id);

    Author findByName(String name);


}
