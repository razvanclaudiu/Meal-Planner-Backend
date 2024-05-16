package com.example.mealplannerbackend.repository;

import com.example.mealplannerbackend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // You can add custom query methods here if needed

}
