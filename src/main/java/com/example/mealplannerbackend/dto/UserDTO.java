package com.example.mealplannerbackend.dto;

import java.util.Date;
import java.util.List;

public class UserDTO {

    private Long id;
    private String username;
    private String password;

    private String email;
    private int level;

    private int experience;
    private List<Long> reviews_id;
    private List<Long> recipes_id;
    private List<Long> awards_id;
    private String name;
    private Date creationDate;
    private String image;
    private String title;


    public UserDTO() {
    }

    public UserDTO(Long id, String username, String password, int level, int experience, List<Long> reviews_id, List<Long> recipes_id, List<Long> awards_id, String name, Date creationDate, String image, String title) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.level = level;
        this.experience = experience;
        this.reviews_id = reviews_id;
        this.recipes_id = recipes_id;
        this.awards_id = awards_id;
        this.name = name;
        this.creationDate = creationDate;
        this.image = image;
        this.title = title;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<Long> getReviews_id() {
        return reviews_id;
    }

    public void setReviews_id(List<Long> reviews_id) {
        this.reviews_id = reviews_id;
    }

    public List<Long> getRecipes_id() {
        return recipes_id;
    }

    public void setRecipes_id(List<Long> recipes_id) {
        this.recipes_id = recipes_id;
    }

    public List<Long> getAwards_id() {
        return awards_id;
    }

    public void setAwards_id(List<Long> awards_id) {
        this.awards_id = awards_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
