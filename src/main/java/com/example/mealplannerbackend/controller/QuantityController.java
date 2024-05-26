package com.example.mealplannerbackend.controller;

import com.example.mealplannerbackend.dto.QuantityDTO;
import com.example.mealplannerbackend.service.QuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quantities")
public class QuantityController {

    @Autowired
    private final QuantityService quantityService;

    public QuantityController(QuantityService quantityService) {
        this.quantityService = quantityService;
    }

    @GetMapping
    public ResponseEntity<List<QuantityDTO>> getAllQuantities() {
        List<QuantityDTO> quantities = quantityService.getAllQuantities();
        return ResponseEntity.ok(quantities);
    }

    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<QuantityDTO>> getQuantitiesOfRecipe(@PathVariable Long recipeId) {
        List<QuantityDTO> quantities = quantityService.getQuantitiesOfRecipe(recipeId);
        return ResponseEntity.ok(quantities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuantityDTO> getQuantityById(@PathVariable Long id) {
        QuantityDTO quantityDTO = quantityService.getQuantityById(id);
        return ResponseEntity.ok(quantityDTO);
    }

    @PostMapping
    public ResponseEntity<QuantityDTO> createQuantity(@RequestBody QuantityDTO quantityDTO) {
        QuantityDTO createdQuantity = quantityService.createQuantity(quantityDTO);
        return ResponseEntity.ok(createdQuantity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuantityDTO> updateQuantity(@PathVariable Long id, @RequestBody QuantityDTO quantityDTO) {
        QuantityDTO updatedQuantity = quantityService.updateQuantity(id, quantityDTO);
        return ResponseEntity.ok(updatedQuantity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuantity(@PathVariable Long id) {
        quantityService.deleteQuantity(id);
        return ResponseEntity.noContent().build();
    }
}
