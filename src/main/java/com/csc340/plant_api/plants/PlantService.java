package com.csc340.plant_api.plants;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService {

    private final PlantRepository repository;

    public PlantService(PlantRepository repository) {
        this.repository = repository;
    }

    public List<Plant> findAll() {
        return repository.findAll();
    }

    public List<Plant> findByLightNeeds(LightNeeds lightNeeds) {
        return repository.findByLightNeeds(lightNeeds);
    }

    // Combines optional filters. Either argument may be null.
    public List<Plant> search(String name, LightNeeds lightNeeds) {
        boolean hasName = name != null && !name.isBlank();

        if (hasName && lightNeeds != null) {
            return repository.findByNameContainingIgnoreCaseAndLightNeeds(name.trim(), lightNeeds);
        }
        if (hasName) {
            return repository.findByNameContainingIgnoreCase(name.trim());
        }
        if (lightNeeds != null) {
            return repository.findByLightNeeds(lightNeeds);
        }
        return repository.findAll();
    }

    public Plant findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new PlantNotFoundException(id));
    }

    public Plant create(Plant plant) {
        return repository.save(plant);
    }

    public Plant update(Long id, Plant updated) {
        Plant existing = findById(id);
        existing.setName(updated.getName());
        existing.setLightNeeds(updated.getLightNeeds());
        existing.setLastWatered(updated.getLastWatered());
        existing.setWateringIntervalDays(updated.getWateringIntervalDays());
        existing.setToxicToPets(updated.isToxicToPets());
        return repository.save(existing);
    }

    public void delete(Long id) {
        findById(id); // throws 404 if missing
        repository.deleteById(id);
    }
}
