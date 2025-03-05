package com.example.librarymanagement.app.entity;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
 


@Getter
@Setter
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "history", schema = "librarymanagementschema")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auditid")
    private Long auditid;
    
    private String borrowed_date;
    private String returned_date;
    private String status;

    @ManyToOne()
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties({"password", "hibernateLazyInitializer", "handler"})
    private UserInfo userInfo;

    @ManyToOne()
    @JoinColumn(name = "bookid")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Books books;

}
