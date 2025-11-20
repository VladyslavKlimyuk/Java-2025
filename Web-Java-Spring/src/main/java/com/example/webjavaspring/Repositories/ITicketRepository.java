package com.example.webjavaspring.Repositories;

import com.example.webjavaspring.Entities.Ticket;
import com.example.webjavaspring.Enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.event.name LIKE %:eventName% AND t.status = 'FREE'")
    List<Ticket> findFreeTicketsByEventName(String eventName);

    List<Ticket> findAllByStatus(TicketStatus status);
}