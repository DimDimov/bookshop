package com.example.buchladen.Repositories;

import com.example.buchladen.Model.Feedback;
import com.example.buchladen.Model.FeedbackVote;
import com.example.buchladen.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackVoteRepository extends JpaRepository<FeedbackVote, Long> {

Optional<FeedbackVote> findByUserAndFeedback(User user, Feedback feedback);

}
