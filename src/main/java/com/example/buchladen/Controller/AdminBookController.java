package com.example.buchladen.Controller;

import com.example.buchladen.Model.*;
import com.example.buchladen.Repositories.*;
import com.example.buchladen.Service.*;
import com.example.buchladen.Utilities.SlugUtil;
import com.example.buchladen.web.dto.AddBookToStockRequest;
import com.example.buchladen.web.dto.BookDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/admin/books")
@Controller
public class AdminBookController {

    private final BookService bookService;

    private final StockService stockService;
    private final AuthorService authorService;
    private final BookDescriptionService bookDescriptionService;


    public AdminBookController( BookService bookService, StockService stockService, AuthorService authorService, BookDescriptionService bookDescriptionService) {

        this.bookService = bookService;
        this.stockService = stockService;
        this.authorService = authorService;
        this.bookDescriptionService = bookDescriptionService;
    }

    @GetMapping("/books_list")
    public String getBooksList(Model model)
    {

        List<Book> books = bookService.findAll();

        model.addAttribute("books", books);
        return "books";
    }

    @PostMapping("/books_list")
    public  ResponseEntity<String> toggleBookStatus(@RequestParam Long bookId,
                                                    @RequestParam boolean active) {

        bookService.toggleActive(bookId, active);
        return  ResponseEntity.ok("Book status updated.");
    }

@GetMapping("/add_newBook")
    public String showBookForm( Model model) {
        model.addAttribute("book", new BookDto());
        model.addAttribute("authors", authorService.findAll());
        return "book_form";
    }

    @PostMapping("/add_newBook")
    public String addBooks (@ModelAttribute("book") BookDto bookDto,
                            BindingResult result
    ) throws IOException {

        if (result.hasErrors()) {
            return "book_form";
        }

        Author  author = authorService.findById(bookDto.getAuthorId());

        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(author);
        book.setIsbn(bookDto.getIsbn());
        book.setGenre(bookDto.getGenre());
        book.setPrice(bookDto.getPrice());

        MultipartFile image = bookDto.getImageFile();

        if(image != null && !image.isEmpty() ) {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            //for intellij
         /*   Path uploadPath = Paths.get("src/main/resources/static/images/books");*/
            //for Docker
            Path uploadPath = Paths.get("/app/uploads/books");

            Files.createDirectories(uploadPath);
            image.transferTo(uploadPath.resolve(fileName));
            //for Docker
            book.setImageBook("/uploads/books/" + fileName);
            //for Intellij
          /* book.setImageBook("/images/books/" + fileName);*/
        }

        bookService.saveBook(book);
        return "redirect:/admin/books/books_list";
    }

    @GetMapping("/books_list/edit_book/{id}")
    public String editBookForm(@PathVariable Long id, Model model)
    {

        Book book = bookService.findById(id);

        BookDto bookDto = new BookDto();
        bookDto.setId((book.getId()));
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthorId(book.getAuthor().getId());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setGenre(book.getGenre());
        bookDto.setExistingImage(book.getImageBook());
        bookDto.setPrice(book.getPrice());
        model.addAttribute("bookEntity", book);
        model.addAttribute("bookDto", bookDto);
        model.addAttribute("authors", authorService.findAll());
        return "edit_book";
    }

    @PostMapping("/books_list/edit_book")
    public String editBook( @Valid @ModelAttribute("bookDto") BookDto bookDto,  BindingResult result,
                           Model model) throws IOException {

        if(result.hasErrors()) {
            model.addAttribute("book", bookDto);
            return "edit_book";
        }

        Long id = bookDto.getId();

        Book book = bookService.findById(id);

        Author author = authorService.findById(bookDto.getAuthorId());

        book.setTitle(bookDto.getTitle());
        book.setAuthor(author);
        book.setIsbn(bookDto.getIsbn());
        book.setGenre(bookDto.getGenre());
        book.setPrice(bookDto.getPrice());

        MultipartFile  image = bookDto.getImageFile();

        if(image != null && !image.isEmpty() ) {
            String newFilename = bookService.saveImage(image);
            book.setImageBook(newFilename);
        } else {
            book.setImageBook(bookDto.getExistingImage());
        }

        bookService.saveBook(book);
        return "redirect:/admin/books/books_list";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
       /*  return ResponseEntity.noContent().build();  // for test*/
    }


    @GetMapping("/editDescription/{id}")
    public String editDescription(@PathVariable Long id, Model model) {

        Book book = bookService.findById(id);

        BookDescription description = book.getDescription();

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());

        if(description != null) {

            bookDto.setDescription(description.getDescription());
        } else {
            bookDto.setDescription("");
        }

        model.addAttribute("book", bookDto);

        return "admin-edit-description";
    }

    @PostMapping("/editDescription")
    public String editDescription(@ModelAttribute("book") BookDto bookDto) {

        Book book = bookService.findById(bookDto.getId());

        BookDescription description = book.getDescription();

        if(description == null) {
            description = new BookDescription();
            description.setBook(book);
        }

        description.setDescription(bookDto.getDescription());

        book.setDescription(description);

        bookDescriptionService.save(description);
        bookService.save(book);

        return "redirect:/admin/books/books_list";
    }

    @PostMapping("/addToStock")
    public ResponseEntity<Map<String, Object>> addToStock(@RequestBody AddBookToStockRequest request) {
        Stock stock = stockService.addNewBookToStock(request);

        Map<String, Object> response = new HashMap<>();
        response.put("title", stock.getBook().getTitle());
        response.put("author", stock.getBook().getAuthor().getName());
        response.put("price", stock.getOneBookPrice());
        if(request.getPrice() == null) {
            throw new IllegalArgumentException("Price must not be null.");
        }

        return ResponseEntity.ok(response);
    }
}


