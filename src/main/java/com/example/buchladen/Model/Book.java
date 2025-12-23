package com.example.buchladen.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="book")
public class Book {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String isbn;
    private String genre;
    private BigDecimal price;
    private String imageBook;
    @Column(name="toggled_book")
    private boolean toggledBook;
    @Column(unique = true)
    private String slug;
    private List<String> previewPages;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    private BookDescription description;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
 private Stock stock;


}
