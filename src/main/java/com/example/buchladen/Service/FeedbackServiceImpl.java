package com.example.buchladen.Service;


import com.example.buchladen.Model.Feedback;
import com.example.buchladen.Model.FeedbackVote;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.FeedbackRepository;
import com.example.buchladen.Repositories.FeedbackVoteRepository;
import com.example.buchladen.Repositories.UserRepository;
import com.example.buchladen.web.dto.FeedbackDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final FeedbackVoteRepository feedbackVoteRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserRepository userRepository, FeedbackVoteRepository feedbackVoteRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.feedbackVoteRepository = feedbackVoteRepository;
    }

    @Transactional
    public Map<String, Object> vote(Long feedbackId, String username, boolean like) {

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback nicht gefunden"));

        User user = userRepository.findByEmailOrCustomUsername(username, username)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        Optional<FeedbackVote> existingVote = feedbackVoteRepository.findByUserAndFeedback(user, feedback);

        if(existingVote.isPresent()) {
            FeedbackVote vote = existingVote.get();

            if(vote.isLike() == like) {
                feedbackVoteRepository.delete(vote);

                if (like) feedback.setLikes(feedback.getLikes() - 1);
                else feedback.setDislikes(feedback.getDislikes() - 1);
            } else {
                 vote.setLike(like);
               feedbackVoteRepository.save(vote);
               if(like) {
                   feedback.setLikes(feedback.getLikes() + 1);
                   feedback.setDislikes(feedback.getDislikes() - 1);
               }
               else{
                   feedback.setLikes(feedback.getLikes() - 1);
                   feedback.setDislikes(feedback.getDislikes() + 1);
               }

            }
        } else {
            FeedbackVote newVote = new FeedbackVote();
            newVote.setUser(user);
            newVote.setFeedback(feedback);
            newVote.setLike(like);
            feedbackVoteRepository.save(newVote);

            if(like) feedback.setLikes(feedback.getLikes() + 1);
            else feedback.setDislikes(feedback.getDislikes() + 1);
        }
        feedbackRepository.save(feedback);

        Map<String, Object> response = new HashMap<>();
        response.put("likes", feedback.getLikes());
        response.put("dislikes", feedback.getDislikes());
        return response;
    }

    @Override
    public double findAverageRatingByBook(Long bookId) {
        return feedbackRepository.findAverageRatingByBook(bookId);
    }

    @Override
    public Long countRatingByBook(Long bookId) {
        return feedbackRepository.countRatingByBook(bookId);
    }

    @Override
    public List<FeedbackDto> findByBookId(Long bookId) {
        return feedbackRepository.findByBookId(bookId);
    }

    @Override
    public Optional<Feedback> findByBookIdAndUserId(Long bookId, Long userId) {
        return feedbackRepository.findByBookIdAndUserId(bookId, userId);
    }

    @Override
    public void save (Feedback feedback) {
        feedbackRepository.save(feedback);
    }
}
