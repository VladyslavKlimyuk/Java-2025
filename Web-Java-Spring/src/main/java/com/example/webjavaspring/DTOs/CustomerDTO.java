package com.example.webjavaspring.DTOs;

import lombok.Data;

@Data
public class CustomerDTO {
    private String name;
    private String email;
    private String phone;
    private String password;
}
