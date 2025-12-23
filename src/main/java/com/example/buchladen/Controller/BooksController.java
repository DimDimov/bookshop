package com.example.buchladen.Controller;


import com.example.buchladen.Mapper.UserMapper;
import com.example.buchladen.Model.Author;
import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.Feedback;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.BookRepository;
import com.example.buchladen.Repositories.FeedbackRepository;
import com.example.buchladen.Repositories.UserRepository;
import com.example.buchladen.Service.*;
import com.example.buchladen.web.dto.BookDto;
import com.example.buchladen.web.dto.CartItemDto;
import com.example.buchladen.web.dto.FeedbackDto;
import com.example.buchladen.web.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@RequestMapping("/books/")
public class BooksController {

    private final BookService bookService;

    private final CartService cartService;

    private final UserService userService;

    private final UserMapper userMapper;

    private final FeedbackServiceImpl feedbackService;

    public BooksController(BookService bookService, CartService cartService, UserService userService, UserMapper userMapper, FeedbackServiceImpl feedbackService) {

        this.bookService = bookService;
        this.cartService = cartService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/genre/Klassik")
    public String getClassicBooks(Model model, Authentication authentication) {

        if(authentication !=null) {

            String login = authentication.getName();
            User user = userService.findByEmailOrCustomUsername(login, login)
                    .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));
            UserDto userDto = userMapper.toDto(user);
            model.addAttribute("user", userDto);
        } else {
            model.addAttribute("user", null);
        }

        return getBooksByGenre("Klassik", model);
    }

    @GetMapping("/genre/Science-fiction")
    public String getScienceFictionBooks(Model model, Authentication authentication) {
        if(authentication !=null) {

            String login = authentication.getName();
            User user = userService.findByEmailOrCustomUsername(login, login)
                    .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));
            UserDto userDto = userMapper.toDto(user);
            model.addAttribute("user", userDto);
        } else {
            model.addAttribute("user", null);
        }
        return getBooksByGenre("Science-fiction", model);
    }

    @GetMapping("/genre/Kinderbuch")
    public String getChildBooks(Model model,Authentication authentication ) {
        if(authentication !=null) {

            String login = authentication.getName();
            User user = userService.findByEmailOrCustomUsername(login, login)
                    .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));
            UserDto userDto = userMapper.toDto(user);
            model.addAttribute("user", userDto);
        } else {
            model.addAttribute("user", null);
        }
        return getBooksByGenre("Kinderbuch", model);
    }

    @GetMapping("/genre/Detektiveroman")
    public String getDetectiveBooks(Model model, Authentication authentication) {
        if(authentication !=null) {

            String login = authentication.getName();
            User user = userService.findByEmailOrCustomUsername(login, login)
                    .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));
            UserDto userDto = userMapper.toDto(user);
            model.addAttribute("user", userDto);
        } else {
            model.addAttribute("user", null);
        }
        return getBooksByGenre("Detektivroman", model);
    }

    private String getBooksByGenre(String genre, Model model) {


        List<Book> booksByGenre = bookService.findBooksByGenre(genre);


        List<Book> toggledBooks = bookService.getToggledBooks().stream()
                .filter(b -> genre.equalsIgnoreCase(b.getGenre())).toList();
        List<BookDto> bookDtos = Stream.concat(booksByGenre.stream(), toggledBooks.stream())
                .distinct()
                .map (book -> {

                 double avg = feedbackService.findAverageRatingByBook(book.getId());
            BookDto dto = new BookDto();

            dto.setId(book.getId());
            dto.setTitle(book.getTitle());
            dto.setAuthorName(book.getAuthor().getName());
            dto.setImageBook(book.getImageBook());
            dto.setPrice(book.getPrice());
                    dto.setSlug(book.getSlug());
                    dto.setAverageRating(avg);

            return dto;
        })
                .toList();

        model.addAttribute("toggledBooks", bookDtos);
        model.addAttribute("selectedGenre", genre);
        return "books_by_genre";
    }

    @GetMapping("/book_info/{slug}")
    public String getBookBySlug(@PathVariable String slug, Model model, Authentication authentication) throws IOException {

        if(authentication !=null) {

            String login = authentication.getName();
            User user = userService.findByEmailOrCustomUsername(login, login)
                    .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden."));

            UserDto userDto = userMapper.toDto(user);

            model.addAttribute("user", userDto);
        }
        else {

            model.addAttribute("user", null);
        }

        Book book = bookService.findBySlug(slug);
        if (book == null) {
            return "home";
        }
        Author author = book.getAuthor();
        double avg = feedbackService.findAverageRatingByBook(book.getId());
        int rounded = (int) Math.round(avg);

           Long ratingCount = feedbackService.countRatingByBook(book.getId());

        //for Intellij
    /*   String pagesFolder = "/images/book_content/" + book.getSlug() + ".pdf";*/
        //for Docker
        String pagesFolder = "/uploads/book_content/" + book.getSlug() + ".pdf";


        List<FeedbackDto> feedbackList = feedbackService.findByBookId(book.getId())
                .stream()
                .map(f -> new FeedbackDto(f.getId(), f.getUsername(),  f.getText(), f.getCreatedAt(), f.getRating(), f.getLikes(), f.getDislikes()))
                .collect(Collectors.toList());

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAverageRating(feedbackService.findAverageRatingByBook(book.getId()));
         bookDto.setRatingCount(feedbackService.countRatingByBook(book.getId()));

        model.addAttribute("feedbackList", feedbackList);
        model.addAttribute("feedback", new FeedbackDto(1L,"", "", null, 0, 0, 0));
        model.addAttribute("rating", avg);
          model.addAttribute("ratingCount", ratingCount);
          model.addAttribute("rounded", rounded);

        model.addAttribute("book", book);
        model.addAttribute("bookPages", pagesFolder);
        model.addAttribute("author", author);

        return "info_about_book";
    }

    @PostMapping("/book_info/{slug}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendSlugToCart(@PathVariable String slug, @RequestBody CartItemDto cartItemDto, Authentication authentication) {

        String login = authentication.getName();

        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow(()-> new RuntimeException("Benutzer nicht gefunden."));

        Book book = bookService.findBySlug(slug);

        cartService.addBookToCart(user, book, cartItemDto.getQuantity());

        int totalQuantity = cartService.getTotalQuantityByUser(user);

        Map<String, Object> response = new HashMap<>();


        response.put("totalQuantity", totalQuantity);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/feedback")
    @ResponseBody
    public ResponseEntity<FeedbackDto> submitFeedback(@RequestParam Long bookId, @RequestParam String text
            , @RequestParam int rating, Authentication authentication) {

        String login = authentication.getName();
        User user = userService.findByEmailOrCustomUsername(login, login)
                .orElseThrow(()-> new RuntimeException("Benutzer nicht gefunden."));

            Book book = bookService.findById(bookId);

            Optional<Feedback> existing = feedbackService.findByBookIdAndUserId(bookId, user.getId());

if (existing.isPresent()) {
    throw new IllegalStateException("Sie haben schon eine Bewertung fuer das Buch verlaesst.");
}

            Feedback feedback = new Feedback();

            feedback.setBook(book);
            feedback.setUser(user);
            feedback.setUsername(user.getFirstName());
            feedback.setText(text);
            feedback.setRating(rating);
            feedback.setCreatedAt(LocalDateTime.now());
            feedbackService.save(feedback);

            FeedbackDto dto = new FeedbackDto(feedback.getId(), feedback.getUsername(), feedback.getText(),
                    feedback.getCreatedAt(), feedback.getRating(), feedback.getLikes(), feedback.getDislikes());

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/feedback/{id}/vote")
    public ResponseEntity<Map<String, Object>> getVote(@PathVariable Long id,
                                                       @RequestParam boolean like,
                                                       Authentication authentication) {
        String username = authentication.getName();
        Map<String, Object> result = feedbackService.vote(id, username, like);
        return  ResponseEntity.ok(result);

    }

}

