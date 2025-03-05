package com.example.librarymanagement.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.librarymanagement.app.entity.Books;
import com.example.librarymanagement.app.entity.History;
import com.example.librarymanagement.app.exception.errorResponse;
import com.example.librarymanagement.app.repository.AuditRepository;
import com.example.librarymanagement.app.repository.BookRepository;
import com.example.librarymanagement.app.service.AuditService;
import com.example.librarymanagement.app.service.BookService;

import jakarta.transaction.Transactional;

@RestController
@Transactional
public class TransactionController {

    @Autowired
    BookService bookService;

    @Autowired
    AuditService auditService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuditRepository auditRepository;


    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('ADMIN')")
    @PostMapping("/borrow")
    public ResponseEntity<History> borrowBook(@RequestBody History history) {
        Books existingBook = bookRepository.findById(history.getBooks().getBookid())
            .orElseThrow(() -> new errorResponse("Book not found with id: " + history.getBooks().getBookid()));

        History existingRecord = auditRepository.findAlreadyBorrowed(
            history.getUserInfo().getId(), 
            history.getBooks().getBookid()
        );

        if (existingRecord != null && "Borrowed".equals(existingRecord.getStatus())) {
            throw new errorResponse("Book is already borrowed: " + history.getBooks().getBookid());
        }

        if ("Available".equals(existingBook.getAvailability())) {
            existingBook.setBooksCount(existingBook.getBooksCount() - 1);
            if (existingBook.getBooksCount() == 0) {
                existingBook.setAvailability("Not Available");
            }

            bookService.addBook(existingBook);
            history.setStatus("Borrowed");
            history.setBorrowed_date(java.time.LocalDate.now().toString());
            History savedHistory = auditService.addRecords(history);
            return ResponseEntity.ok(savedHistory);
        }

        throw new errorResponse("Book is not available: " + history.getBooks().getBookid());
    }

    @PostMapping("/return")
    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('ADMIN')")
    public ResponseEntity<History> returnBook(@RequestBody History history) {
        Books existingBook = bookRepository.findById(history.getBooks().getBookid())
            .orElseThrow(() -> new errorResponse("Book not found with id: " + history.getBooks().getBookid()));

        History existingRecord = auditRepository.findByUserAndBook(
            history.getUserInfo().getId(), 
            history.getBooks().getBookid()
        ).orElseThrow(() -> new errorResponse(
            "No borrow record found for user: " + history.getUserInfo().getId() + 
            " and book: " + history.getBooks().getBookid()
        ));

        if (!"Borrowed".equals(existingRecord.getStatus())) {
            throw new errorResponse("Book is not borrowed: " + history.getBooks().getBookid());
        }

        existingBook.setAvailability("Available");
        existingBook.setBooksCount(existingBook.getBooksCount() + 1);
        bookService.addBook(existingBook);

        existingRecord.setStatus("Returned");
        existingRecord.setReturned_date(java.time.LocalDate.now().toString());
        History updatedRecord = auditService.addRecords(existingRecord);

        return ResponseEntity.ok(updatedRecord);
    }

    @GetMapping("/history")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<History>> getHistory() {
        return ResponseEntity.ok(auditRepository.findAllWithUserAndBook());
    }

    @GetMapping("/getHistoryByUserOrBook")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<History> getHistoryByUserAndBook(@RequestParam Map<String, String> queryParams) {
        List<History> records =  auditService.getHistoryBasedOnQueryParams(queryParams);
        if(records.isEmpty()){
            throw new errorResponse("No records found with the given query params");
        }
        return records;
        
    }


}   
