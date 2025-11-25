package com.example.buchladen.Service;


import com.example.buchladen.Model.Author;
import com.example.buchladen.Model.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface BookService {

    List<Book> getToggledBooks();

    Optional<Book> getBookId(Long id);

    void saveBook(Book book);

    List<Book> findAllBooks(String query1, String query2);

    void toggleActive(Long bookId, boolean active);

    void deleteBook(Long id);


    String saveImage(MultipartFile file) throws IOException;

    List<Book>findBooksByGenre(String genre);


    Book findBySlug(String book);

    Book findById(Long id);

    List<Book> findAll();

    void save(Book book);

}