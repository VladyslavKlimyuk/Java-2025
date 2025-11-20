package com.example.webjavaspring.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketPackDTO {
    private BigDecimal cost;
    private int count;
}
