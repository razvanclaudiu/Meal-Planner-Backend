package com.example.mealplannerbackend.service;

import com.example.mealplannerbackend.dto.IngredientDTO;
import com.example.mealplannerbackend.exceptions.IngredientNotFoundException;
import com.example.mealplannerbackend.model.Ingredient;
import com.example.mealplannerbackend.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final ModelMapper modelMapper;

    public List<IngredientDTO> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<IngredientDTO> getAllIngredientsOfRecipe(Long id) {
        List<Ingredient> ingredients = ingredientRepository.getAllByRecipesId(id);
        return ingredients.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public IngredientDTO getIngredientById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient not found with id: " + id));
        return convertToDto(ingredient);
    }

    public IngredientDTO createIngredient(IngredientDTO ingredientDTO) {
        Ingredient ingredient = convertToEntity(ingredientDTO);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return convertToDto(savedIngredient);
    }

    public IngredientDTO updateIngredient(Long id, IngredientDTO updatedIngredientDTO) {
        // Convert the updated DTO to an entity
        Ingredient updatedIngredient = convertToEntity(updatedIngredientDTO);

        // Retrieve the existing ingredient from the repository
        Ingredient existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient with id " + id + " not found"));

        // Update the fields of the existing ingredient with the new values
        existingIngredient.setName(updatedIngredient.getName());

        // Save the updated ingredient
        Ingredient savedIngredient = ingredientRepository.save(existingIngredient);

        // Convert the saved ingredient entity back to DTO and return
        return convertToDto(savedIngredient);
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }

    private IngredientDTO convertToDto(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientDTO.class);
    }

    private Ingredient convertToEntity(IngredientDTO ingredientDTO) {
        return modelMapper.map(ingredientDTO, Ingredient.class);
    }


}
