package com.example.buchladen.Repositories;

import com.example.buchladen.Model.BookDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDescriptionRepository extends JpaRepository<BookDescription, Long> {
}
