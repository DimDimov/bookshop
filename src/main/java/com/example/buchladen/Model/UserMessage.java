package com.example.buchladen.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="user_message")
public class UserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    private User sender;

    private boolean read_flag = false;
    private boolean answered = false;
}
