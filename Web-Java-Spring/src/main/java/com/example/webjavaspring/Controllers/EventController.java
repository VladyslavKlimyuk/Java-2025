package com.example.webjavaspring.Controllers;

import com.example.webjavaspring.Entities.*;
import com.example.webjavaspring.DTOs.*;
import com.example.webjavaspring.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public String home(Model model) {
        List<Event> upcomingEvents = eventService.findUpcomingEvents();
        model.addAttribute("events", upcomingEvents);
        model.addAttribute("searchQuery", "");
        return "index";
    }

    @GetMapping("/search")
    public String searchTickets(@RequestParam(name = "eventName", required = false) String eventName, Model model) {
        List<Ticket> freeTickets = eventService.findFreeTicketsByEvent(eventName);

        model.addAttribute("freeTickets", freeTickets);
        model.addAttribute("searchQuery", eventName);
        return "search_results";
    }

    @GetMapping("/buy/{ticketId}")
    public String showBuyForm(@PathVariable Long ticketId, Model model) {
        model.addAttribute("ticketId", ticketId);
        model.addAttribute("customer", new CustomerDTO());
        return "buy_form";
    }

    @PostMapping("/buy/{ticketId}")
    public String processPurchase(@PathVariable Long ticketId, @ModelAttribute CustomerDTO customerDTO, Model model) {
        try {
            Customer customer = eventService.createCustomer(customerDTO);
            Long customerId = customer.getId();

            Ticket soldTicket = eventService.assignTicketToCustomer(ticketId, customerId);

            model.addAttribute("ticket", soldTicket);
            return "purchase_success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "purchase_error";
        }
    }

    @GetMapping("/create")
    public String showEventCreateForm(Model model) {
        EventCreationDTO dto = new EventCreationDTO();
        dto.setTicketPacks(List.of(new TicketPackDTO(), new TicketPackDTO()));

        model.addAttribute("eventCreationDTO", dto);
        return "event_create";
    }

    @PostMapping("/create")
    public String processEventCreation(@ModelAttribute EventCreationDTO dto, Model model) {
        try {
            eventService.createEvent(dto);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Помилка створення події: " + e.getMessage());
            return "purchase_error";
        }
    }

    @GetMapping("/customers/create")
    public String showCustomerCreateForm(Model model) {
        model.addAttribute("customerDTO", new CustomerDTO());
        return "customer_create";
    }

    @PostMapping("/customers/create")
    public String processCustomerCreation(@ModelAttribute CustomerDTO dto, Model model) {
        try {
            eventService.createCustomer(dto);
            return "redirect:/api/events";
        } catch (Exception e) {
            model.addAttribute("error", "Помилка реєстрації: " + e.getMessage());
            return "purchase_error";
        }
    }
}