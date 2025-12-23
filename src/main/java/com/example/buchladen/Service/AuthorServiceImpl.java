package com.example.buchladen.Service;


import com.example.buchladen.Model.Author;
import com.example.buchladen.Repositories.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    //for Intellij
    private final String uploadDir = "src/main/resources/static/images/authors/";//for Intellij

    //for Docker
    private final String uploadDockerDir = "/app/uploads/authors";//for Docker
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public String saveImage(MultipartFile file) throws IOException{
        if (file.isEmpty()) {
            return null;
        }

        Path uploadPath = Paths.get(uploadDockerDir);
        Files.createDirectories(uploadPath);

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());
        //for Intellij
      /*  return "/images/authors/" + fileName;*/
        //for Docker
       return "/uploads/authors/" + fileName;

    }

    @Override
    public Author findByName(String name) {
        return authorRepository.findByName(name)
                .orElseThrow(()-> new RuntimeException("Autor nicht gefunden."));
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public void save(Author author) {
        authorRepository.save(author);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor nicht gefunden."));
    }
}
