package com.example.buchladen.Repositories;

import com.example.buchladen.Model.Author;
import com.example.buchladen.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByToggledBookTrue();

    List<Book>findByGenreContainsIgnoreCase(String genre);

    List<Book>findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCase(String title, String authorName);

    Optional<Book>findBySlug(String slug);

    Optional<Book> findByTitle(String title);
}
