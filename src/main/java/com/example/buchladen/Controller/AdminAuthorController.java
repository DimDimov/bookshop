package com.example.buchladen.Controller;


import com.example.buchladen.Model.Author;
import com.example.buchladen.Repositories.AuthorRepository;
import com.example.buchladen.Service.AuthorService;
import com.example.buchladen.web.dto.AuthorDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RequestMapping("/admin/authors")
@Controller
public class AdminAuthorController {

    private final AuthorService authorService;

    public AdminAuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/author_list")
    public String getAuthorList(Model model) {

        List<Author> authors = authorService.findAll();

        model.addAttribute("authors", authors);

        return "author";
    }

    @GetMapping("/add_newAuthor")
    public String showAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "author_form";
    }

    @PostMapping("/add_newAuthor")
    public String addAuthor(@ModelAttribute("author") AuthorDto authorDto,
                            BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "author_form";
        }

        Author author = new Author();
        author.setName(authorDto.getName());
        author.setBiography(authorDto.getBiography());
        MultipartFile photo = authorDto.getPhotoUrl();

        if (photo != null && !photo.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + photo.getOriginalFilename();
           /* Path uploadPath = Paths.get("src/main/resources/static/images/authors");*///for Intellij
            Path uploadPath = Paths.get("/app/uploads/authors");
            Files.createDirectories(uploadPath);
            photo.transferTo(uploadPath.resolve(fileName));
            /*author.setPhotoUrl("/images/authors/" + fileName);*///for intellij
            author.setPhotoUrl("/uploads/authors/" + fileName);
        }
        authorService.save(author);

        return "redirect:/admin/authors/author_list";
    }

    @GetMapping("/author_list/edit_author/{id}")
    public String editAuthorForm(@PathVariable Long id, Model model) {

        Author author = authorService.findById(id);

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setBiography(author.getBiography());
        authorDto.setExistingPhoto(author.getPhotoUrl());

        model.addAttribute("authorEntity", author);
        model.addAttribute("authorDto", authorDto);

        return "edit_author";
    }

    @PostMapping("/author_list/edit_author")
    public String editAuthor(@Valid @ModelAttribute("author") AuthorDto authorDto,
                             BindingResult result,
                             Model model) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("author", authorDto);
            return "edit_author";
        }

            Author author = authorService.findById(authorDto.getId());

            if (author.getId() == null) {
                throw new IllegalArgumentException("Author ID must not be null when editing.");
            }

            author.setName(authorDto.getName());
            author.setBiography(authorDto.getBiography());
            author.setPhotoUrl(authorDto.getExistingPhoto());

            MultipartFile photo = authorDto.getPhotoUrl();
            if (photo != null && !photo.isEmpty()) {
                String newFileName = authorService.saveImage(photo);
                author.setPhotoUrl(newFileName);
            } else {
                author.setPhotoUrl(authorDto.getExistingPhoto());
            }
           /* model.addAttribute("author", author);*/
            authorService.save(author);
            return "redirect:/admin/authors/author_list";
        }

        @DeleteMapping("/author_list/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok().build();
        }
}
