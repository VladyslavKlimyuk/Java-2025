package com.example.webjavaspring.Repositories;

import com.example.webjavaspring.Entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByNameContainingIgnoreCase(String name);
}