package com.example.librarymanagement.app.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.librarymanagement.app.entity.Books;

public interface BookRepository extends JpaRepository<Books, Long> {

     @Query(value = "SELECT * FROM librarymanagementschema.books b where b.title=:title and b.author=:author", nativeQuery = true)
    public Books findBytitleAndAuthor(@Param("title") String title, @Param("author") String author);
 
    
}