package com.example.librarymanagement.app.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "books", schema = "librarymanagementschema")
public class Books implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookid")
    private Long bookid;

    private String title;
    private String author;
    private String category;
    private String availability;
    
    @Column(name = "books_count")
    private Integer booksCount;

}
