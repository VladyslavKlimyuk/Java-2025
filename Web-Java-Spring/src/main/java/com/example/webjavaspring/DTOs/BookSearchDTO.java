package com.example.webjavaspring.DTOs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchDTO {
    private String title;
    private String author;
}