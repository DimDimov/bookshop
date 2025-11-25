package com.example.buchladen.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class BookRating {

    @Id
    @GeneratedValue

    private Long id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private User user;

    private int rating;

}
