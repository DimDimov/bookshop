package com.example.buchladen.Controller;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/uploads")
public class UploadImagesInDockerController {

    @GetMapping("/authors/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        Path  file = Paths.get("uploads/authors").resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok().body(resource);
    }

    @GetMapping("/books/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getBookImage(@PathVariable String filename) throws IOException {
        Path  file = Paths.get("uploads/books").resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok().body(resource);
    }

}
