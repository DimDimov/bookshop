package com.example.buchladen.Controller;


import com.example.buchladen.Model.*;
import com.example.buchladen.Repositories.ReturnedBooksRepository;
import com.example.buchladen.Service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/admin/control")
@Controller
public class AdminReturnedBoughtBooksController {

    private final OrderServiceImpl orderService;
    private final ReturnedBookServiceImpl returnedBookService;
    private final BookService bookService;
    private final UserService userService;
    private final OrderItemService orderItemService;

    public AdminReturnedBoughtBooksController(OrderServiceImpl orderService, ReturnedBookServiceImpl returnedBookService, BookService bookService, UserService userService, OrderItemService orderItemService) {

        this.orderService = orderService;
        this.returnedBookService = returnedBookService;
        this.bookService = bookService;
        this.userService = userService;
        this.orderItemService = orderItemService;
    }

    @GetMapping("/controlForBooks")
    public String controlForBooks(@RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Long userId,
            Model model) {


        List<OrderItem> items;

        List<Book> books = bookService.findAll();

        List<User> users = userService.findAllUsers();

        if(bookId != null && userId != null) {
            Book book = bookService.findById(bookId);
            User user = userService.findById(userId);
            items = orderItemService.findByOrder_UserAndBook(user, book);
        } else if(bookId != null) {
            Book book = bookService.findById(bookId);
            items = orderItemService.findByBook(book);
        } else if (userId != null) {
            User user = userService.findById(userId);
            items = orderItemService.findByOrder_User(user);
        }
        else {
            items = orderItemService.findAll();
        }

        BigDecimal profit =  BigDecimal.valueOf( orderService.calculateProfit());

        model.addAttribute("items", items);
        model.addAttribute("books", books);
        model.addAttribute("users", users);
        model.addAttribute("selectedBookId", bookId);
        model.addAttribute("selectedUserId", userId);
        model.addAttribute("profit", profit);

        return "admin-books-control";
    }

    @PostMapping("/order-item/{id}/return")
    public ResponseEntity<Map<String, Object>> markReturned(@PathVariable Long id){
        OrderItem item = orderItemService.findById(id);

        Map<String, Object> response = new HashMap<>();

        if(!item.isReturned()) {
            item.setReturned(true);
            orderItemService.save(item);

            ReturnedBook returnedBook = new ReturnedBook();
            returnedBook.setBook(item.getBook());
            returnedBook.setOrder(item.getOrder());
            returnedBook.setUser(item.getOrder().getUser());
            returnedBook.setQuantity((int) item.getQuantity());
            returnedBook.setReturnedDate(LocalDateTime.now());
            returnedBookService.save(returnedBook);

     BigDecimal profit =  BigDecimal.valueOf( orderService.calculateProfit());

            response.put("status", "returned");
            response.put("profit", profit);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/returned-books")
    public String getReturnedBooks(Model model) {
        List<ReturnedBook> returns = returnedBookService.findAll();
        model.addAttribute("returns", returns);
        return "admin-returned-books";
    }

    @DeleteMapping("/delete-returned-book/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteReturnedBook(@PathVariable Long id){
        returnedBookService.deleteReturnedBookById(id);
        return ResponseEntity.ok().build();
    }
}
