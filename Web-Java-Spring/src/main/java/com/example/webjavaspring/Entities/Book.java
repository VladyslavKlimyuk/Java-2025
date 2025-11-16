package com.example.webjavaspring.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private Integer publicationYear;

    private String publisher;

    private String genre;

    private Integer pageCount;

    @Column(length = 2000)
    private String description;

    private Integer circulation;
}