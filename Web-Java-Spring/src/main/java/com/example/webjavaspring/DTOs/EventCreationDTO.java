package com.example.webjavaspring.DTOs;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventCreationDTO {
    private String name;
    private LocalDateTime eventDate;
    private PlaceDTO place;
    private List<TicketPackDTO> ticketPacks;
}