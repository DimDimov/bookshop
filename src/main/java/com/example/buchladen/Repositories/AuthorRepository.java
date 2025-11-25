package com.example.buchladen.Repositories;

import com.example.buchladen.Model.Author;
import com.example.buchladen.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
   Optional<Author> findByName(String authorName);
}
