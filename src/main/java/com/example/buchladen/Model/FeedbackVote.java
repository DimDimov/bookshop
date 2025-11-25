package com.example.buchladen.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name ="feedback_vote",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"book_id", "user_id"})
        }
)
public class FeedbackVote {

    @Id
    @GeneratedValue

    private Long id;

    @ManyToOne
    private Feedback feedback;

    @ManyToOne
    private User user;

    @Column(name = "is_like")
    private boolean like;

}
