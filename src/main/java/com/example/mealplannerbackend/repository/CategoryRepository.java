package com.example.mealplannerbackend.repository;

import com.example.mealplannerbackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // You can add custom query methods here if needed
}

