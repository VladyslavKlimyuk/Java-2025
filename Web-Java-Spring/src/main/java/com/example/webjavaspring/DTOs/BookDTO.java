package com.example.webjavaspring.DTOs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private Integer publicationYear;
    private String publisher;
    private String genre;
    private Integer pageCount;
    private String description;
    private Integer circulation;
}