package com.example.webjavaspring.Repositories;

import com.example.webjavaspring.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByEventDateAfterOrderByEventDateAsc(LocalDateTime now);

    Optional<Event> findByNameContainingIgnoreCase(String name);
}