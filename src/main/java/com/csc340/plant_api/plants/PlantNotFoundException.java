package com.csc340.plant_api.plants;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlantNotFoundException extends RuntimeException {
    public PlantNotFoundException(Long id) {
        super("Plant " + id + " not found");
    }
}
