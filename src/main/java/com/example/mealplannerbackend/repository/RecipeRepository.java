package com.example.mealplannerbackend.repository;

import com.example.mealplannerbackend.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> getAllByUserId(Long id);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.categories c WHERE c.id IN :categoryIds GROUP BY r HAVING COUNT(DISTINCT c) = :categoryCount")
    List<Recipe> findByCategoriesIds(@Param("categoryIds") List<Long> categoryIds, @Param("categoryCount") Long categoryCount);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredients i WHERE i.id IN :ingredientIds GROUP BY r HAVING COUNT(DISTINCT i) = :ingredientCount")
    List<Recipe> findByIngredientsIds(@Param("ingredientIds") List<Long> ingredientIds, @Param("ingredientCount") Long ingredientCount);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.categories c JOIN r.ingredients i WHERE c.id IN :categoryIds AND i.id IN :ingredientIds GROUP BY r HAVING COUNT(DISTINCT c) = :categoryCount AND COUNT(DISTINCT i) = :ingredientCount")
    List<Recipe> findByCategoriesIdsAndIngredientsIds(@Param("categoryIds") List<Long> categoryIds, @Param("ingredientIds") List<Long> ingredientIds, @Param("categoryCount") Long categoryCount, @Param("ingredientCount") Long ingredientCount);

    @Query(value = "SELECT coalesce(max(id), 0) FROM Recipe")
    public Long getMaxId();
}
