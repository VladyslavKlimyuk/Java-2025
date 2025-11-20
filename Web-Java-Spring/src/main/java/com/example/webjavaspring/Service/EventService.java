package com.example.webjavaspring.Service;

import com.example.webjavaspring.Entities.*;
import com.example.webjavaspring.Repositories.*;
import com.example.webjavaspring.DTOs.*;
import com.example.webjavaspring.Enums.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final IEventRepository eventRepository;
    private final IPlaceRepository placeRepository;
    private final ITicketRepository ticketRepository;
    private final ICustomerRepository customerRepository;

    @Transactional
    public Event createEvent(EventCreationDTO dto) {
        Place place = placeRepository.findByNameContainingIgnoreCase(dto.getPlace().getName())
                .orElseGet(() -> placeRepository.save(Place.builder()
                        .name(dto.getPlace().getName())
                        .address(dto.getPlace().getAddress())
                        .build()));

        Event event = Event.builder()
                .name(dto.getName())
                .eventDate(dto.getEventDate())
                .place(place)
                .build();
        event = eventRepository.save(event);

        List<Ticket> tickets = new ArrayList<>();
        int seatCounter = 1;

        for (TicketPackDTO pack : dto.getTicketPacks()) {
            for (int i = 0; i < pack.getCount(); i++) {
                Ticket ticket = Ticket.builder()
                        .cost(pack.getCost())
                        .number("S-" + seatCounter++)
                        .status(TicketStatus.FREE)
                        .event(event)
                        .build();
                tickets.add(ticket);
            }
        }
        ticketRepository.saveAll(tickets);

        event.setTickets(tickets);
        return event;
    }

    @Transactional
    public Customer createCustomer(CustomerDTO dto) {
        return customerRepository.findByEmail(dto.getEmail())
                .orElseGet(() -> customerRepository.save(Customer.builder()
                        .name(dto.getName())
                        .email(dto.getEmail())
                        .phone(dto.getPhone())
                        .build()));
    }

    public List<Ticket> findFreeTicketsByEvent(String eventName) {
        return ticketRepository.findFreeTicketsByEventName(eventName);
    }

    public List<Event> findUpcomingEvents() {
        return eventRepository.findAll();
    }

    @Transactional
    public Ticket assignTicketToCustomer(Long ticketId, Long customerId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Квиток не знайдено"));

        if (ticket.getStatus() == TicketStatus.SOLD) {
            throw new RuntimeException("Квиток вже проданий");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        ticket.setStatus(TicketStatus.SOLD);
        ticket.setCustomer(customer);
        return ticketRepository.save(ticket);
    }
}