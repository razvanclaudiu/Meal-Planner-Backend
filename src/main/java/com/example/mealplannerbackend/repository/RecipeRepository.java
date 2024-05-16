package com.example.mealplannerbackend.repository;

import com.example.mealplannerbackend.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> getAllByUserId(Long id);

    @Query(value = "SELECT coalesce(max(id), 0) FROM Recipe")
    public Long getMaxId();
}
