package com.example.mealplannerbackend.controller;

import com.example.mealplannerbackend.dto.ReviewDTO;
import com.example.mealplannerbackend.service.AwardService;
import com.example.mealplannerbackend.service.RecipeService;
import com.example.mealplannerbackend.service.ReviewService;
import com.example.mealplannerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    private final RecipeService recipeService;

    private final AwardService awardService;

    private final UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, RecipeService recipeService, AwardService awardService, UserService userService) {
        this.reviewService = reviewService;
        this.recipeService = recipeService;
        this.awardService = awardService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsOfRecipe(@PathVariable("id") Long id) {
        List<ReviewDTO> reviews = reviewService.getAllReviewsOfRecipe(id);
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsOfUser(@PathVariable("id") Long id) {
        List<ReviewDTO> reviews = reviewService.getAllReviewsOfUser(id);
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable("id") Long id) {
        ReviewDTO reviewDTO = reviewService.getReviewById(id);
        return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.createReview(reviewDTO);
        recipeService.updateRating(createdReview.getRecipe_id());
        awardService.checkAndAwardAchievements(reviewDTO.getUser_id());
        awardService.checkAndAwardAchievements(userService.getUserByUsername(recipeService.getRecipeById(reviewDTO.getRecipe_id()).getUsername()).getId());
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable("id") Long id, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("id") Long id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
