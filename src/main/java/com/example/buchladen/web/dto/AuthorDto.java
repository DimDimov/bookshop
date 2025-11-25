package com.example.buchladen.web.dto;


import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

    private Long id;
    private String name;
    private MultipartFile photoUrl;
    private String existingPhoto;
    @Lob
    private String biography;

}
