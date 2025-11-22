package com.example.webjavaspring.Controllers;

import com.example.webjavaspring.DTOs.CustomerDTO;
import com.example.webjavaspring.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final EventService eventService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("customerDTO", new CustomerDTO());
        return "customer_create";
    }

    @PostMapping
    public String processRegistration(@ModelAttribute CustomerDTO dto, Model model) {
        try {
            eventService.createCustomer(dto);
            return "redirect:/api/events/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Помилка реєстрації: " + e.getMessage());
            return "purchase_error";
        }
    }
}