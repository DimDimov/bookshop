package com.example.buchladen.Service;

import com.example.buchladen.Model.Feedback;
import com.example.buchladen.web.dto.FeedbackDto;

import java.util.List;
import java.util.Optional;

public interface FeedbackService {

    double findAverageRatingByBook(Long bookId);

    Long countRatingByBook(Long bookId);

    List<FeedbackDto> findByBookId(Long bookId);

    Optional<Feedback> findByBookIdAndUserId(Long bookId, Long userId);

    void save (Feedback feedback);
}
