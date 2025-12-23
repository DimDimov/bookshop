package com.example.buchladen.Repositories;

import com.example.buchladen.Model.Feedback;
import com.example.buchladen.Model.FeedbackVote;
import com.example.buchladen.Model.User;
import com.example.buchladen.web.dto.FeedbackDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT coalesce( AVG(f.rating), 0) FROM Feedback f WHERE f.book.id = :bookId")
    Double findAverageRatingByBook(@Param("bookId") Long bookId);

    @Query("SELECT  count (f.rating) FROM Feedback  f WHERE  f.book.id = :bookId")
    Long countRatingByBook(@Param("bookId") Long bookId);

    @Query("SELECT f FROM Feedback f WHERE f.book.id = :bookId ORDER BY f.createdAt DESC")
    List<Feedback>findByBookId(@Param("bookId") Long bookId);

    Optional<Feedback>findByBookIdAndUserId(Long bookId, Long UserId);

}
