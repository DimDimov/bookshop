package com.example.buchladen.Service;


import com.example.buchladen.Model.Book;
import com.example.buchladen.Repositories.BookRepository;
import com.example.buchladen.Utilities.SlugUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{


    private  final BookRepository bookRepository;

    //for Intellij
    private final String uploadDir = "src/main/resources/static/images/books/";

    //for Docker
    private final String uploadDockerDir = "/app/uploads/books";

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getToggledBooks() {
        return bookRepository.findByToggledBookTrue();
    }

    @Override
    public Optional<Book> getBookId(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void saveBook(Book book) {

        String slug = SlugUtil.toSlug(book.getTitle());
        book.setSlug(slug);

        bookRepository.save(book);
    }

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }


    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book findById(Long id){
        return bookRepository.findById(id).orElseThrow( () -> new RuntimeException("Buch nicht gefunden.") );
    }

    @Override
    public List<Book> findAllBooks(String query1, String query2) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCase(query1, query2);
    }


@Override
@Transactional
    public void toggleActive (Long bookId, boolean active) {
    Optional <Book> optionalBook = bookRepository.findById(bookId);
    if (optionalBook.isPresent()) {
        Book book = optionalBook.get();
        book.setToggledBook(active);
        bookRepository.save(book);
    }
    }

    public String saveImage (MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        Path uploadPath = Paths.get(uploadDockerDir);
        Files.createDirectories(uploadPath);

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePpath = uploadPath.resolve(filename);
        Files.write(filePpath, file.getBytes());
        //for Intellij
       /* return "/images/books/" + filename;*/
        return "/uploads/books/" + filename;
    }

    @Override
    public List<Book>findBooksByGenre(String genre) {
        return bookRepository.findByGenreContainsIgnoreCase(genre);
    }


    @Override
    public Book findBySlug(String slug) {

        return bookRepository.findBySlug(slug)
                .orElseThrow(()-> new RuntimeException("Buch nicht gefunden bei Slug."));
    }
}
