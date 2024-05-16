package com.example.mealplannerbackend.dto;

import com.example.mealplannerbackend.model.Recipe;
import com.example.mealplannerbackend.model.User;

public class ReviewDTO {

    private Long id;
    private Long user_id;
    private Long recipe_id;
    private String description;
    private int rating; // Out of 5
    private String image;

    // Constructors, Getters, and Setters...

    public ReviewDTO() {
    }

    public ReviewDTO(Long id, Long user_id, Long recipe_id, String description, int rating, String image) {
        this.id = id;
        this.user_id = user_id;
        this.recipe_id = recipe_id;
        this.description = description;
        this.rating = rating;
        this.image = image;
    }

    // Getters and Setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return user_id;
    }

    public void setUser(Long user_id) {
        this.user_id = user_id;
    }

    public Long getRecipe(){
        return recipe_id;
    }

    public void setRecipe(Long recipe_id){
        this.recipe_id = recipe_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
