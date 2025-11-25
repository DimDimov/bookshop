package com.example.buchladen.Service;

import com.example.buchladen.Model.BookDescription;
import com.example.buchladen.Repositories.BookDescriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class BookDescriptionServiceImpl implements BookDescriptionService{

    private final BookDescriptionRepository bookDescriptionRepository;

    public BookDescriptionServiceImpl(BookDescriptionRepository bookDescriptionRepository) {
        this.bookDescriptionRepository = bookDescriptionRepository;
    }

    @Override
   public void save(BookDescription description) {
        bookDescriptionRepository.save(description);
    }
}
