package com.example.mealplannerbackend.repository;

import com.example.mealplannerbackend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> getAllByRecipesId(Long id);
}
