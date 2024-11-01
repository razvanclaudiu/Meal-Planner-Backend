package com.example.mealplannerbackend.repository;

import com.example.mealplannerbackend.model.Quantity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuantityRepository extends JpaRepository<Quantity, Long> {
    List<Quantity> findAllByRecipeId(Long recipeId);
    @Transactional
    void deleteAllByRecipeId(Long recipeId);
}
