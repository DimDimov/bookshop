package com.example.buchladen.Service;

import com.example.buchladen.Model.*;
import com.example.buchladen.Repositories.AuthorRepository;
import com.example.buchladen.Repositories.BookRepository;
import com.example.buchladen.Repositories.StockRepository;
import com.example.buchladen.web.dto.AddBookToStockRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public StockServiceImpl(StockRepository stockRepository, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.stockRepository = stockRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Stock> findAll(){
        return stockRepository.findAll();
    }

    @Override
    public void save(Stock stock){
        stockRepository.save(stock);
    }

    @Override
    public Stock findById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Lager nicht gefunden."));
    }

    @Override
    public Author findByName(String name) {
        return authorRepository.findByName(name)
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(name);
                    return newAuthor;
                });
    }

    public void deleteBookFromStock(Long id) {
        stockRepository.deleteById(id);
    }

    @Override
    public Stock addNewBookToStock(AddBookToStockRequest request) {
        Author author = authorRepository.findByName(request.getAuthorName())
                .orElseGet(() -> {

                    Author newAuthor = new Author();
                    newAuthor.setName(request.getAuthorName());
                    return authorRepository.save(newAuthor);
                });
        Book book = bookRepository.findByTitle(request.getTitle())
                .orElseGet(() -> {
                    Book newBook = new Book();
                    newBook.setTitle(request.getTitle());
                    newBook.setPrice(request.getPrice());
                    newBook.setAuthor(author);
                    return bookRepository.save(newBook);
                });

        Stock stock = stockRepository.findByBook(book)
                .orElseGet(() -> {

                    Stock newStock = new Stock();
                    newStock.setBook(book);
                    newStock.setQuantity(1);
                    return newStock;
                });


        stock.setOneBookPrice(request.getPrice());
        stock.calculateTotalPrice();
        stock.setLastUpdate(LocalDateTime.now());
        return stockRepository.save(stock);
    }

    @Transactional
    public void reduceStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Stock stock = item.getStock();

            int newQuantity = (int) (stock.getQuantity() - item.getQuantity());
            if (newQuantity < 0) {
                throw new RuntimeException("Nicht genug Buecher im Lager: " + stock.getBook().getTitle());
            }

            stock.setQuantity(newQuantity);
            stock.calculateTotalPrice();
            stockRepository.save(stock);
        }
    }

  /*  @Transactional
    public void restoreStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Stock stock = item.getStock();
            stock.setQuantity((int) (stock.getQuantity() + item.getQuantity()));
            stockRepository.save(stock);
        }
    }*/

    @Override
    public double calculateCostFullStock() {
        List<Stock> stocks = stockRepository.findAll();
        double totalStockPrice = 0.0;

        for(Stock stock : stocks) {
            if(stock.getQuantity() > 0) {

                totalStockPrice += stock.getQuantity() * stock.getBook().getPrice().doubleValue();

            }
        }
        return totalStockPrice;
    }


}