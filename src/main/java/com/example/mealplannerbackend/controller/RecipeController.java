package com.example.mealplannerbackend.controller;

import com.example.mealplannerbackend.dto.RecipeDTO;
import com.example.mealplannerbackend.service.AwardService;
import com.example.mealplannerbackend.service.RecipeService;
import com.example.mealplannerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    private final AwardService awardService;

    private final UserService userService;

    @Autowired
    public RecipeController(RecipeService recipeService, AwardService awardService, UserService userService) {
        this.recipeService = recipeService;
        this.awardService = awardService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        List<RecipeDTO> recipes = recipeService.getAllRecipes();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<List<String>> getAllCategoriesOfRecipe(@PathVariable Long id){
        List<String> categories = recipeService.getAllCategoriesOfRecipe(id);
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @GetMapping("/{id}/ingredients")
    public ResponseEntity<List<String>> getAllIngredientsOfRecipe(@PathVariable Long id){
        List<String> ingredients = recipeService.getAllIngredientsOfRecipe(id);
        return new ResponseEntity<>(ingredients,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id) {
        RecipeDTO recipe = recipeService.getRecipeById(id);
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecipeDTO>> getRecipeByUserId(@PathVariable Long userId){
        List<RecipeDTO> recipes = recipeService.getAllRecipesOfUser(userId);
        return new ResponseEntity<>(recipes,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) {
        RecipeDTO newRecipe = recipeService.createRecipe(recipeDTO);
        awardService.checkAndAwardAchievements(userService.getUserByUsername(recipeDTO.getUsername()).getId());
        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO updatedRecipeDTO) {
        RecipeDTO updatedRecipe = recipeService.updateRecipe(id, updatedRecipeDTO);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeDTO>> filterRecipesByKeyword(@RequestParam String keyword) {
        List<RecipeDTO> filteredRecipes = recipeService.filterRecipesByKeyword(keyword);
        return new ResponseEntity<>(filteredRecipes, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RecipeDTO>> filterRecipesByCategoriesAndIngredients(
            @RequestParam(value = "categories", required = false) List<Long> filterCategories,
            @RequestParam(value = "ingredients", required = false) List<Long> filterIngredients) {

        if ((filterCategories == null || filterCategories.isEmpty()) &&
                (filterIngredients == null || filterIngredients.isEmpty())) {
            return getAllRecipes();
        }

        List<RecipeDTO> filteredRecipes = recipeService.filterRecipesByCategoriesAndIngredients(filterCategories, filterIngredients);
        return new ResponseEntity<>(filteredRecipes, HttpStatus.OK);
    }

}
