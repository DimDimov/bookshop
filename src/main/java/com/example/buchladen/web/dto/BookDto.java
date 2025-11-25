package com.example.buchladen.web.dto;

import com.example.buchladen.Model.Author;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long id;
    private  Long authorId;
    private String authorName;

    private String title;
    private String isbn;
    private String genre;
    @Lob
    private String description;
    private BigDecimal price;
   private String imageBook;
    private MultipartFile imageFile;
    private String existingImage;
    private boolean toggledBook = false;
    private String slug;

    private double averageRating;

    private long ratingCount;

}
