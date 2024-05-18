package com.example.mealplannerbackend.repository;

import com.example.mealplannerbackend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT coalesce(max(id), 0) FROM Review")
    public Long getMaxId();

    List<Review> getAllByRecipeId(Long id);
}
