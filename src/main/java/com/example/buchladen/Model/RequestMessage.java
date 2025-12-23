package com.example.buchladen.Model;


import com.example.buchladen.Enums.SenderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class RequestMessage {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CustomerRequest request;

    @ManyToOne
    private User sender;

    private String text;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private SenderType senderType;



    private boolean readByUser = false;
    private boolean readByAdmin = false;

    @ManyToOne
    private Book book;

}
