package com.example.mealplannerbackend.service;

import com.example.mealplannerbackend.dto.ReviewDTO;
import com.example.mealplannerbackend.exceptions.ReviewNotFoundException;
import com.example.mealplannerbackend.model.Recipe;
import com.example.mealplannerbackend.model.Review;
import com.example.mealplannerbackend.model.User;
import com.example.mealplannerbackend.repository.RecipeRepository;
import com.example.mealplannerbackend.repository.ReviewRepository;
import com.example.mealplannerbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<ReviewDTO> getAllReviewsOfRecipe(Long id) {
        List<Review> reviews = reviewRepository.getAllByRecipeId(id);
        return reviews.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<ReviewDTO> getAllReviewsOfUser(Long id) {
        List<Review> reviews = reviewRepository.getAllByUserId(id);
        return reviews.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));
        return convertToDto(review);
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Review review = convertToEntity(reviewDTO);
        Review savedReview = reviewRepository.save(review);
        savedReview.setImage("review_"+savedReview.getId()+".jpeg");
        savedReview = reviewRepository.save(savedReview);
        return convertToDto(savedReview);
    }

    public ReviewDTO updateReview(Long id, ReviewDTO updatedReviewDTO) {
        Review updatedReview = convertToEntity(updatedReviewDTO);

        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + id + " not found"));

        existingReview.setUser(updatedReview.getUser());
        existingReview.setRecipe(updatedReview.getRecipe());
        existingReview.setDescription(updatedReview.getDescription());
        existingReview.setRating(updatedReview.getRating());
        existingReview.setImage(updatedReview.getImage());

        Review savedReview = reviewRepository.save(existingReview);

        return convertToDto(savedReview);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    private ReviewDTO convertToDto(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setUser_id(review.getUser().getId());
        reviewDTO.setRecipe_id(review.getRecipe().getId());
        reviewDTO.setDescription(review.getDescription());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setImage(review.getImage());
        return reviewDTO;
    }

    private Review convertToEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        User user = userRepository.findById(reviewDTO.getUser_id()).orElse(null);
        Recipe recipe = recipeRepository.findById(reviewDTO.getRecipe_id()).orElse(null);
        review.setUser(user);
        review.setRecipe(recipe);
        review.setDescription(reviewDTO.getDescription());
        review.setRating(reviewDTO.getRating());
        review.setImage(reviewDTO.getImage());
        return review;
    }



}
