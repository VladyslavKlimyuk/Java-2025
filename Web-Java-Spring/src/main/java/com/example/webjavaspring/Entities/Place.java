package com.example.webjavaspring.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "place")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String address;
}