package com.example.mealplannerbackend.service;

import com.example.mealplannerbackend.dto.RecipeDTO;
import com.example.mealplannerbackend.exceptions.RecipeNotFoundException;
import com.example.mealplannerbackend.model.*;
import com.example.mealplannerbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;

    public List<RecipeDTO> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<RecipeDTO> getAllRecipesOfUser(Long id) {
        List<Recipe> recipes = recipeRepository.getAllByUserId(id);
        return recipes.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<String> getAllCategoriesOfRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + id));
        List<String> categories = new ArrayList<>();
        for (Category category : recipe.getCategories()){
            categories.add(category.getName());
        }
        return categories;
    }

    public List<String> getAllIngredientsOfRecipe(Long id){
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + id));
        List<String> ingredients = new ArrayList<>();
        for(Ingredient ingredient : recipe.getIngredients()){
            ingredients.add(ingredient.getName());
        }
        return ingredients;
    }

    public RecipeDTO getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + id));
        return convertToDto(recipe);
    }

    public RecipeDTO createRecipe(RecipeDTO recipeDTO) {
        Recipe recipe = convertToEntity(recipeDTO);
        Recipe savedRecipe = recipeRepository.save(recipe);
        savedRecipe.setImage("recipe_"+savedRecipe.getId()+".jpeg");
        savedRecipe = recipeRepository.save(savedRecipe);
        return convertToDto(savedRecipe);
    }

    public RecipeDTO updateRecipe(Long id, RecipeDTO updatedRecipeDTO) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with id " + id + " not found"));

        var optionalUser = userRepository.findByUsername(updatedRecipeDTO.getUsername());
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));

        existingRecipe.setTitle(updatedRecipeDTO.getTitle());
        existingRecipe.setImage(updatedRecipeDTO.getImage());
        existingRecipe.setMethod(updatedRecipeDTO.getMethod());
        existingRecipe.setTimeToCook(updatedRecipeDTO.getTimeToCook());
        existingRecipe.setRating(updatedRecipeDTO.getRating());
        existingRecipe.setUser(user);
        existingRecipe.setVideoLink(updatedRecipeDTO.getVideoLink());

        List<Ingredient> ingredients = ingredientRepository.findAllById(updatedRecipeDTO.getIngredients_id());
        List<Category> categories = categoryRepository.findAllById(updatedRecipeDTO.getCategories_id());

        existingRecipe.setIngredients(ingredients);
        existingRecipe.setCategories(categories);

        Recipe savedRecipe = recipeRepository.save(existingRecipe);

        return convertToDto(savedRecipe);
    }

    public void updateRating(Long id){
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with id " + id + " not found"));
        List<Review> reviews = existingRecipe.getReviews();
        if (reviews.size() >= 5) {
            int sumOfRatings = reviews.stream().mapToInt(Review::getRating).sum();
            existingRecipe.setRating((double) sumOfRatings / reviews.size());

        }
        else existingRecipe.setRating(0);


        recipeRepository.save(existingRecipe);
    }


    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    private List<Long> getReviewIds(Recipe recipe) {
        List<Review> reviews = recipe.getReviews();
        List<Long> reviewIds = new ArrayList<>();
        for (Review review : reviews) {
            reviewIds.add(review.getId());
        }
        return reviewIds;
    }

    private List<Long> getCategoryIds(Recipe recipe){
        List<Category> categories = recipe.getCategories();
        List<Long> categoryIds = new ArrayList<>();
        for (Category category : categories){
            categoryIds.add(category.getId());
        }
        return categoryIds;
    }

    private List<Long> getIngredientIds(Recipe recipe){
        List<Ingredient> ingredients = recipe.getIngredients();
        List<Long> ingredientIds = new ArrayList<>();
        for(Ingredient ingredient : ingredients){
            ingredientIds.add(ingredient.getId());
        }
        return ingredientIds;
    }

    public List<RecipeDTO> filterRecipesByKeyword(String keyword) {
        List<Recipe> recipes = recipeRepository.findAll();
        if (keyword.isEmpty())
            return recipes.stream().map(this::convertToDto).collect(Collectors.toList());
        return recipes.stream()
                .filter(recipe -> recipe.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RecipeDTO convertToDto(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        recipeDTO.setTitle(recipe.getTitle());
        recipeDTO.setImage(recipe.getImage());
        recipeDTO.setMethod(recipe.getMethod());
        recipeDTO.setTimeToCook(recipe.getTimeToCook());
        recipeDTO.setRating(recipe.getRating());
        recipeDTO.setUsername(recipe.getUser().getUsername());
        recipeDTO.setVideoLink(recipe.getVideoLink());
        recipeDTO.setIngredients_id(getIngredientIds(recipe));
        recipeDTO.setReviews_id(getReviewIds(recipe));
        recipeDTO.setCategories_id(getCategoryIds(recipe));

        return recipeDTO;
    }

    private Recipe convertToEntity(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeDTO.getId());
        recipe.setTitle(recipeDTO.getTitle());
        recipe.setMethod(recipeDTO.getMethod());
        recipe.setTimeToCook(recipeDTO.getTimeToCook());
        recipe.setRating(recipeDTO.getRating());
        var optionalUser = userRepository.findByUsername(recipeDTO.getUsername());
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));
        recipe.setUser(user);
        recipe.setVideoLink(recipeDTO.getVideoLink());
        List<Ingredient> ingredients = new ArrayList<>();
        if(recipeDTO.getIngredients_id() != null){
            for(Long ingredientId : recipeDTO.getIngredients_id()){
                Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);
                if(ingredient != null){
                    ingredients.add(ingredient);
                }
            }
        }
        recipe.setIngredients(ingredients);
        List<Review> reviews = new ArrayList<>();
        if(recipeDTO.getReviews_id() != null) {
            for (Long reviewId : recipeDTO.getReviews_id()) {
                Review review = reviewRepository.findById(reviewId).orElse(null);
                if (review != null) {
                    reviews.add(review);
                }
            }
        }
        recipe.setReviews(reviews);
        List<Category> categories = new ArrayList<>();
        if(recipeDTO.getCategories_id() != null){
            for(Long categoryId : recipeDTO.getCategories_id()){
                Category category = categoryRepository.findById(categoryId).orElse(null);
                if(category != null){
                    categories.add(category);
                }
            }
        }
        recipe.setCategories(categories);
        return recipe;
    }

    public List<RecipeDTO> filterRecipesByCategoriesAndIngredients(List<Long> filterCategories, List<Long> filterIngredients) {
        if ((filterCategories == null || filterCategories.isEmpty()) &&
                (filterIngredients == null || filterIngredients.isEmpty())) {
            return getAllRecipes();
        }

        List<Recipe> recipes;

        if (filterCategories != null && !filterCategories.isEmpty() &&
                filterIngredients != null && !filterIngredients.isEmpty()) {
            recipes = recipeRepository.findByCategoriesIdsAndIngredientsIds(filterCategories, filterIngredients, (long) filterCategories.size(), (long) filterIngredients.size());
        } else if (filterCategories != null && !filterCategories.isEmpty()) {
            recipes = recipeRepository.findByCategoriesIds(filterCategories, (long) filterCategories.size());
        } else {
            recipes = recipeRepository.findByIngredientsIds(filterIngredients, (long) filterIngredients.size());
        }
        return recipes.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
