package com.example.mealplannerbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quantities")
public class Quantity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long recipeId;

    @Column(nullable = false)
    private Long ingredientId;

    private String quantity;
    public Quantity() {
    }

    public Quantity(Long recipeId, Long ingredientId, String quantity) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
