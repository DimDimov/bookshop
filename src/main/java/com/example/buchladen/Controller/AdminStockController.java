package com.example.buchladen.Controller;


import com.example.buchladen.Enums.StockStatus;
import com.example.buchladen.Model.Author;
import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.Stock;
import com.example.buchladen.Repositories.AuthorRepository;
import com.example.buchladen.Repositories.BookRepository;
import com.example.buchladen.Repositories.StockRepository;
import com.example.buchladen.Service.AuthorService;
import com.example.buchladen.Service.BookService;
import com.example.buchladen.Service.StockService;
import com.example.buchladen.Service.StockServiceImpl;
import com.example.buchladen.web.dto.BookDto;
import com.example.buchladen.web.dto.StockDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/admin/stock")
@Controller
public class AdminStockController {

    private final StockService stockService;
    private final AuthorService authorService;
    private final BookService bookService;

    public AdminStockController(StockService stockService, AuthorService authorService, BookService bookService) {
        this.stockService = stockService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping("/books/getStock")
    public String getStock(Model model) {

        List<Stock> stock = stockService.findAll();

        BigDecimal totalStockValue = BigDecimal.valueOf(stockService.calculateCostFullStock());

        model.addAttribute("stocks", stock);
        model.addAttribute("totalStockValue", totalStockValue);

        return "admin-books-stockPage";
    }

    @GetMapping("/addNewBookToStock")
    public String addNewBookToStock(Model model){

        StockDto stockDto = new StockDto();
        stockDto.setBookDto(new BookDto());

        model.addAttribute("stock", stockDto);

        return "stock-form-page";
    }

    @PostMapping("/addNewBookToStock")
    public String addNewBook(@ModelAttribute("stock") StockDto stockDto) {

        String authorName = stockDto.getBookDto().getAuthorName();

        Author author = authorService.findByName(authorName);

        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(stockDto.getBookDto().getTitle());
        bookService.save(book);

        Stock stock = new Stock();
        stock.setBook(book);
        stock.setStockStatus(StockStatus.NEW);
        stock.setOneBookPrice(stockDto.getOneBookPrice());
        stock.setQuantity(stockDto.getQuantity());
        stock.setLastUpdate(LocalDateTime.now());

        stock.calculateTotalPrice();

        stockService.save(stock);

        return "redirect:/admin/books/getStock";
    }

    @GetMapping("/books/getStock/edit_stock/{id}")
    public String editStock(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);

        Book book = stock.getBook();

        BookDto bookDto = new BookDto();

        bookDto.setTitle(stock.getBook().getTitle());
        bookDto.setAuthorName(book.getAuthor().getName());

        StockDto stockDto = new StockDto();
        stockDto.setId(stock.getId());
        stockDto.setStockStatus(stock.getStockStatus());
        stockDto.setLastUpdate(LocalDateTime.now());
        stockDto.setQuantity(stock.getQuantity());
        stockDto.setOneBookPrice(stock.getOneBookPrice());
        stockDto.setBookDto(bookDto);

        model.addAttribute("stock", stockDto);
        return "admin_edit_stock";
    }

    @PostMapping("/books/getStock/edit_stock")
    public String editStock(@ModelAttribute("stock") StockDto stockDto, BindingResult result, Model model){

        if(result.hasErrors()) {
            model.addAttribute("stock", stockDto);
            return "admin_edit_stock";}

        Long id = stockDto.getId();

        Stock stock = stockService.findById(stockDto.getId());

        Book book = stock.getBook();
        Author author = book.getAuthor();

        BookDto bookDto = stockDto.getBookDto();
        if(bookDto != null) {
            book.setTitle(bookDto.getTitle());
            if(author != null) {
                author.setName(bookDto.getAuthorName());
                authorService.save(author);
            }
        }

        stock.setStockStatus(stockDto.getStockStatus());
        stock.setLastUpdate(stockDto.getLastUpdate());
        stock.setOneBookPrice(stockDto.getOneBookPrice());
        stock.setQuantity(stockDto.getQuantity());
        stock.calculateTotalPrice();

        bookService.save(book);
        stockService.save(stock);

        return "redirect:/admin/stock/books/getStock";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteBookFromStock(@PathVariable Long id) {
        stockService.deleteBookFromStock(id);
        return ResponseEntity.ok().build();
    }

}
