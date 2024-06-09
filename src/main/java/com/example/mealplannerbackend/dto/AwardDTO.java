package com.example.mealplannerbackend.dto;

import java.util.List;

public class AwardDTO {

    private Long id;
    private String name;
    private String description;
    private String image;

    private List<Long> users_id;


    public AwardDTO() {
    }

    public AwardDTO(Long id, String name, String description, String image, List<Long> users_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.users_id = users_id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Long> getUsers_id() {
        return users_id;
    }

    public void setUsers_id(List<Long> users_id) {
        this.users_id = users_id;
    }
}
