package com.example.mealplannerbackend.service;

import com.example.mealplannerbackend.dto.QuantityDTO;
import com.example.mealplannerbackend.exceptions.QuantityNotFoundException;
import com.example.mealplannerbackend.model.Quantity;
import com.example.mealplannerbackend.repository.QuantityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuantityService {

    @Autowired
    private final QuantityRepository quantityRepository;

    public List<QuantityDTO> getAllQuantities() {
        List<Quantity> quantities = quantityRepository.findAll();
        return quantities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<QuantityDTO> getQuantitiesOfRecipe(Long recipeId) {
        List<Quantity> quantities = quantityRepository.findAllByRecipeId(recipeId);
        return quantities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public QuantityDTO getQuantityById(Long id) {
        Quantity quantity = quantityRepository.findById(id)
                .orElseThrow(() -> new QuantityNotFoundException("Quantity not found with id: " + id));
        return convertToDto(quantity);
    }

    public QuantityDTO createQuantity(QuantityDTO quantityDTO) {
        Quantity quantity = convertToEntity(quantityDTO);
        Quantity savedQuantity = quantityRepository.save(quantity);
        return convertToDto(savedQuantity);
    }

    public QuantityDTO updateQuantity(Long id, QuantityDTO updatedQuantityDTO) {
        Quantity existingQuantity = quantityRepository.findById(id)
                .orElseThrow(() -> new QuantityNotFoundException("Quantity with id " + id + " not found"));

        existingQuantity.setRecipeId(updatedQuantityDTO.getRecipeId());
        existingQuantity.setIngredientId(updatedQuantityDTO.getIngredientId());
        existingQuantity.setQuantity(updatedQuantityDTO.getQuantity());

        Quantity savedQuantity = quantityRepository.save(existingQuantity);
        return convertToDto(savedQuantity);
    }

    public void deleteQuantity(Long id) {
        quantityRepository.deleteById(id);
    }

    public void deleteQuantitiesOfRecipe(Long id) {
        quantityRepository.deleteAllByRecipeId(id);
    }

    private QuantityDTO convertToDto(Quantity quantity) {
        QuantityDTO quantityDTO = new QuantityDTO();
        quantityDTO.setId(quantity.getId());
        quantityDTO.setRecipeId(quantity.getRecipeId());
        quantityDTO.setIngredientId(quantity.getIngredientId());
        quantityDTO.setQuantity(quantity.getQuantity());
        return quantityDTO;
    }

    private Quantity convertToEntity(QuantityDTO quantityDTO) {
        Quantity quantity = new Quantity();
        quantity.setId(quantityDTO.getId());
        quantity.setRecipeId(quantityDTO.getRecipeId());
        quantity.setIngredientId(quantityDTO.getIngredientId());
        quantity.setQuantity(quantityDTO.getQuantity());
        return quantity;
    }


}
