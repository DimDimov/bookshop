package com.example.buchladen.Controller;

import com.example.buchladen.Mapper.UserMapper;
import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.CustomerRequest;
import com.example.buchladen.Model.User;
import com.example.buchladen.Service.*;
import com.example.buchladen.web.dto.BookDto;
import com.example.buchladen.web.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {


    private final PreferenceService preferenceService;
    private final BookService bookService;
    private  final CustomerRequestService customerRequestService;
    private final UserService userService;
    private final FeedbackService feedbackService;
    private final UserMapper userMapper;

    public HomeController(BookService bookService, PreferenceService preferenceService, CustomerRequestService customerRequestService, UserService userService, FeedbackService feedbackService, UserMapper userMapper) {

        this.bookService = bookService;
        this.preferenceService = preferenceService;
        this.customerRequestService = customerRequestService;
        this.userService = userService;
        this.feedbackService = feedbackService;
        this.userMapper = userMapper;
    }

    @GetMapping("/home")
    public String getHome1(Model model, Authentication authentication) {

        if(authentication == null || !authentication.isAuthenticated()) {

            model.addAttribute("user", null);

        } else {

            String email = authentication.getName();

            User user = userService.findByEmailOrCustomUsername(email, email)
                    .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

            Long latestRequestId;

            List<CustomerRequest> request = customerRequestService.findByCustomer(user);

            UserDto userDto = userMapper.toDto(user);//for test


            latestRequestId = request.stream()
                    .filter(r -> r.getMessages().stream()
                            .anyMatch(m -> !m.isReadByUser()
                                    && m.getSender() != null))
                    .map(CustomerRequest::getId)
                    .findFirst()
                    .orElse(null);

            model.addAttribute("user", userDto);//for test*/
            model.addAttribute("latestRequestId", latestRequestId);
        }

        List<Book> toggledBooks = bookService.getToggledBooks();
        List<BookDto> bookDtos = toggledBooks.stream().map(book -> {
                    double avg = feedbackService.findAverageRatingByBook(book.getId());

                    BookDto dto = new BookDto();
                    dto.setId(book.getId());
                    dto.setTitle(book.getTitle());
                    dto.setAuthorName(book.getAuthor().getName());
                    dto.setPrice(book.getPrice());
                    dto.setImageBook(book.getImageBook());
                    dto.setAverageRating(avg);
                    dto.setSlug(book.getSlug());

                    return dto;
                })
                .toList();

        model.addAttribute("toggledBooks", bookDtos);

        return "home";
    }

    @GetMapping("/login")
    public String getLogin() {

        return "login";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Book> searchBook(@RequestParam("query") String query, Model model) {

        List<Book> books = bookService.findAllBooks(query, query);

        model.addAttribute("books", books);
        model.addAttribute("query", query);

        return bookService.findAllBooks(query, query);
    }

    @PostMapping("/preferences/add")
    public String addPreference(@RequestParam Long userId, @RequestParam Long bookId) {
        preferenceService.addBookPreference(userId, bookId);
        return "redirect:/home";
    }

}

