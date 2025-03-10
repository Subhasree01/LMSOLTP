package com.example.librarymanagement.app.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.librarymanagement.app.entity.Books;
import com.example.librarymanagement.app.exception.errorResponse;
import com.example.librarymanagement.app.repository.BookRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;


@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;


    @CacheEvict(value = "books", allEntries = true)
    public Books addBook(Books book) {
         return bookRepository.save(book);
    }

    @CacheEvict(value = "books", key = "#bookid")
    public void deleteBook(Long bookid) {
        Books book = bookRepository.findById(bookid)
        .orElseThrow(() -> new errorResponse("No Books found with this id: " + bookid));
        bookRepository.delete(book);
    }

    @Cacheable(value = "books", key = "#queryParams.toString()")
    public List<Books> getBooksBasedOnQueryParams(Map<String, String> queryParams) {
        System.out.println("Fetching Books from DB");
        Iterable<Books> allBooks = bookRepository.findAll();
        return StreamSupport.stream(allBooks.spliterator(), false)
                .filter(book -> {
                    boolean matches = true;
                    for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                        switch (entry.getKey()) {
                            case "title":
                                matches = matches && book.getTitle().equalsIgnoreCase(entry.getValue());
                                break;
                            case "author":
                                matches = matches && book.getAuthor().equalsIgnoreCase(entry.getValue());
                                break;
                            case "category":
                                matches = matches && book.getCategory().equalsIgnoreCase(entry.getValue());
                                break;
                            case "availability":
                                matches = matches && book.getAvailability().equalsIgnoreCase(entry.getValue());
                                break;
                            case "books_count":
                                matches = matches && book.getBooksCount() == Integer.parseInt(entry.getValue());
                                break;
                            default:
                                matches = false;
                        }
                    }
                    return matches;
                })
                .collect(Collectors.toList());
    }


}
