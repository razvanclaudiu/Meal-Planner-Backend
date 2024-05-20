package com.example.mealplannerbackend.service;

import com.example.mealplannerbackend.dto.RecipeDTO;
import com.example.mealplannerbackend.exceptions.RecipeNotFoundException;
import com.example.mealplannerbackend.model.*;
import com.example.mealplannerbackend.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository, ReviewRepository reviewRepository, IngredientRepository ingredientRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
    }

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
        return convertToDto(savedRecipe);
    }

    public RecipeDTO updateRecipe(Long id, RecipeDTO updatedRecipeDTO) {
        // Retrieve the existing recipe from the repository
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with id " + id + " not found"));

        var optionalUser = userRepository.findByUsername(updatedRecipeDTO.getUsername());
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));

        // Update the fields of the existing recipe with the new values
        existingRecipe.setTitle(updatedRecipeDTO.getTitle());
        existingRecipe.setImage(updatedRecipeDTO.getImage());
        existingRecipe.setMethod(updatedRecipeDTO.getMethod()); // Changed from getDescription
        existingRecipe.setTimeToCook(updatedRecipeDTO.getTimeToCook());
        existingRecipe.setRating(updatedRecipeDTO.getRating());
        existingRecipe.setUser(user); // Changed from getAuthor
        existingRecipe.setVideoLink(updatedRecipeDTO.getVideoLink());

        List<Ingredient> ingredients = ingredientRepository.findAllById(updatedRecipeDTO.getIngredients_id());
        List<Category> categories = categoryRepository.findAllById(updatedRecipeDTO.getCategories_id());

        existingRecipe.setIngredients(ingredients); // Changed from getIngredients
        existingRecipe.setCategories(categories);

        // Save the updated recipe
        Recipe savedRecipe = recipeRepository.save(existingRecipe);

        // Convert the saved recipe entity back to DTO and return
        return convertToDto(savedRecipe);
    }

    public void updateRating(Long id){
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with id " + id + " not found"));
        List<Review> reviews = existingRecipe.getReviews();
        if (reviews.size() >= 5) {
            int sumOfRatings = reviews.stream().mapToInt(Review::getRating).sum();
            existingRecipe.setRating((double) sumOfRatings / reviews.size());
            recipeRepository.save(existingRecipe);
        }
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
        recipeDTO.setUsername(recipe.getUser().getUsername()); // Assuming you want to get the username
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
        Long recipeNumber = recipeRepository.getMaxId();
        recipeNumber++;
        recipe.setImage("recipe_" + recipeNumber + ".jpeg");
        recipe.setMethod(recipeDTO.getMethod());
        recipe.setTimeToCook(recipeDTO.getTimeToCook());
        recipe.setRating(recipeDTO.getRating());
        // Assuming you have a method to find User by username
        System.out.println(recipeDTO.getUsername());
        var optionalUser = userRepository.findByUsername(recipeDTO.getUsername());
        User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));
        recipe.setUser(user);
        recipe.setVideoLink(recipeDTO.getVideoLink());
        List<Ingredient> ingredients = new ArrayList<>();
        System.out.println(recipeDTO.getIngredients_id());
        System.out.println(recipeDTO.getCategories_id());
        if(recipeDTO.getIngredients_id() != null){
            for(Long ingredientId : recipeDTO.getIngredients_id()){
                Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);
                if(ingredient != null){
                    ingredients.add(ingredient);
                }
            }
        }
        System.out.println(ingredients);
        recipe.setIngredients(ingredients);
        // Assuming you have a method to find Review by its ID
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
        System.out.println(categories);
        recipe.setCategories(categories);
        return recipe;
    }

}
