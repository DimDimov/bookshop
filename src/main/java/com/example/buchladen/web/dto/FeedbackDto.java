package com.example.buchladen.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class FeedbackDto {

    private Long id;
    private String username;
    private String text;
    private LocalDateTime createdAt;
    private int rating;
    private int likes;
    private int dislikes;


   /* public FeedbackDto(Long id, String username, String text, LocalDateTime createdAt, int rating) {
    }*/
}
