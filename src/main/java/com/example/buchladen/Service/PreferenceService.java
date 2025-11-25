package com.example.buchladen.Service;


import com.example.buchladen.Model.Book;
import com.example.buchladen.Model.User;
import com.example.buchladen.Repositories.BookRepository;
import com.example.buchladen.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PreferenceService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public PreferenceService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public void addBookPreference(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow();
        Book book = bookRepository.findById(bookId).orElseThrow();

        if(!user.getPreferredBooks().contains(book)) {
            user.getPreferredBooks().add(book);
            userRepository.save(user);
        }
    }
}
