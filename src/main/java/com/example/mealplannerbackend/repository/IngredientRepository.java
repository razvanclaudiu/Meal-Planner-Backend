package com.example.mealplannerbackend.repository;

import com.example.mealplannerbackend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // You can add custom query methods here if needed
}
