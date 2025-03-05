package com.example.librarymanagement.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.librarymanagement.app.entity.Books;
import com.example.librarymanagement.app.exception.errorResponse;
import com.example.librarymanagement.app.repository.BookRepository;
import com.example.librarymanagement.app.service.BookService;

import jakarta.transaction.Transactional;

@RestController
@Transactional
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Books addBook(@RequestBody Books books) {
        Books existingBook = bookRepository.findBytitleAndAuthor(books.getTitle(), books.getAuthor());
        Integer book_count = books.getBooksCount() !=null? books.getBooksCount() : 1;
        Books b = null;
        if(existingBook!=null){
            existingBook.setBooksCount(existingBook.getBooksCount() + book_count);
            b = bookService.addBook(existingBook);
        }else{
            books.setBooksCount(book_count);
            System.out.println("book_count" + book_count);
            b = bookService.addBook(books);
        }
        return b;
    }

    @PutMapping("/{bookid}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Books updateBook(@PathVariable Long bookid, @RequestBody Books books) {
        Books existingBook = bookRepository.findById(bookid)
        .orElseThrow(() -> new errorResponse("Book not found with id: " + bookid));
        System.out.println("books.getBooksCount()" + books.getBooksCount());
        existingBook.setAvailability(books.getAvailability() != null ? books.getAvailability() : existingBook.getAvailability());
        existingBook.setAuthor(books.getAuthor() != null ? books.getAuthor() : existingBook.getAuthor());
        existingBook.setCategory(books.getCategory() != null ? books.getCategory() : existingBook.getCategory());
        existingBook.setTitle(books.getTitle() != null ? books.getTitle() : existingBook.getTitle());
        existingBook.setBooksCount(books.getBooksCount() != null ? books.getBooksCount() : existingBook.getBooksCount());

        return bookService.addBook(existingBook);
    }

    @DeleteMapping("/{bookid}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteBook(@PathVariable Long bookid) {
        bookService.deleteBook(bookid);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('ADMIN')")
    public List<Books> getBooksBasedOnQueryParams(@RequestParam Map<String, String> queryParams) {
        return bookService.getBooksBasedOnQueryParams(queryParams);
    }

}