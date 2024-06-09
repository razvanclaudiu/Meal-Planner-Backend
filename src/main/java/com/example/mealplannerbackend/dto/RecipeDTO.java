package com.example.mealplannerbackend.dto;

import com.example.mealplannerbackend.model.User;
import com.example.mealplannerbackend.model.Review;
import java.util.List;

public class RecipeDTO {

    private Long id;
    private String title;
    private String image;
    private String method;
    private String timeToCook;
    private double rating;
    private String username;
    private String videoLink;
    private List<Long> ingredients_id;
    private List<Long> reviews_id;
    private List<Long> categories_id;


    public RecipeDTO() {
    }

    public RecipeDTO(Long id, String title, String image, String method, String timeToCook, double rating, String username, String videoLink, List<Long> ingredients_id, List<Long> reviews_id, List<Long> categories_id) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.method = method;
        this.timeToCook = timeToCook;
        this.rating = rating;
        this.username = username;
        this.videoLink = videoLink;
        this.ingredients_id = ingredients_id;
        this.reviews_id = reviews_id;
        this.categories_id = categories_id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTimeToCook() {
        return timeToCook;
    }

    public void setTimeToCook(String timeToCook) {
        this.timeToCook = timeToCook;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public List<Long> getIngredients_id() {
        return ingredients_id;
    }

    public void setIngredients_id(List<Long> ingredients_id) {
        this.ingredients_id = ingredients_id;
    }

    public List<Long> getReviews_id() {
        return reviews_id;
    }

    public void setReviews_id(List<Long> reviews_id) {
        this.reviews_id = reviews_id;
    }

    public List<Long> getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(List<Long> categories_id) {
        this.categories_id = categories_id;
    }
}
