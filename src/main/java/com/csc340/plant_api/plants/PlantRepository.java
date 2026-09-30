package com.csc340.plant_api.plants;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, Long> {

    // Derived query: Spring writes the SQL from the method name.
    List<Plant> findByLightNeeds(LightNeeds lightNeeds);

    // Partial, case-insensitive match: "mon" finds "Monstera".
    // Spring reads the method name: Name + Containing + IgnoreCase.
    List<Plant> findByNameContainingIgnoreCase(String name);

    // Both filters at once.
    List<Plant> findByNameContainingIgnoreCaseAndLightNeeds(String name, LightNeeds lightNeeds);
}
