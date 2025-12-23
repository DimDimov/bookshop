package com.example.buchladen.Model;


import jakarta.persistence.*;

@Entity
public class BookRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private User user;

    private int rating;

}
