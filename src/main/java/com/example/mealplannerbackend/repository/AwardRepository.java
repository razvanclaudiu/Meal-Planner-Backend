package com.example.mealplannerbackend.repository;

import com.example.mealplannerbackend.model.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    Award getAwardByName(String name);
}
