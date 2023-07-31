package com.venturi.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

//Lombok annotations
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "book_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "borrower_id")
    @JsonIgnore
    private Long borrowerId;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name="book_author",
            joinColumns= {@JoinColumn(name="book_id")},
            inverseJoinColumns = {@JoinColumn(name="author_id")}
    )
    private Set<Author> bookAuthors = new HashSet<>();


}
