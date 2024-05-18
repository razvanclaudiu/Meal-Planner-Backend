package com.example.mealplannerbackend.service;

import com.example.mealplannerbackend.dto.UserDTO;
import com.example.mealplannerbackend.exceptions.UserNotFoundException;
import com.example.mealplannerbackend.model.Award;
import com.example.mealplannerbackend.model.Recipe;
import com.example.mealplannerbackend.model.Review;
import com.example.mealplannerbackend.model.User;
import com.example.mealplannerbackend.repository.AwardRepository;
import com.example.mealplannerbackend.repository.RecipeRepository;
import com.example.mealplannerbackend.repository.ReviewRepository;
import com.example.mealplannerbackend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RecipeRepository recipeRepository;

    private final ReviewRepository reviewRepository;

    private final AwardRepository awardRepository;

    @Autowired
    public UserService(UserRepository userRepository, RecipeRepository recipeRepository, ReviewRepository reviewRepository, AwardRepository awardRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.reviewRepository = reviewRepository;
        this.awardRepository = awardRepository;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with usenname: " + username));
        return convertToDto(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO updatedUserDTO) {
        // Convert the updated DTO to an entity
        User updatedUser = convertToEntity(updatedUserDTO);

        // Retrieve the existing user from the repository
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        // Update the fields of the existing user with the new values
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setLevel(updatedUser.getLevel());
        existingUser.setExperience(updatedUser.getExperience());
        // Update other fields similarly...

        // Save the updated user
        User savedUser = userRepository.save(existingUser);

        // Convert the saved user entity back to DTO and return
        return convertToDto(savedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setLevel(user.getLevel());
        userDTO.setExperience(user.getExperience());

        // Extracting ids from related entities
        List<Long> reviewIds = user.getReviews().stream()
                .map(Review::getId)
                .collect(Collectors.toList());
        userDTO.setReviews_id(reviewIds);

        List<Long> recipeIds = user.getRecipes().stream()
                .map(Recipe::getId)
                .collect(Collectors.toList());
        userDTO.setRecipes_id(recipeIds);

        List<Long> awardIds = user.getAwards().stream()
                .map(Award::getId)
                .collect(Collectors.toList());
        userDTO.setAwards_id(awardIds);

        userDTO.setName(user.getName());
        userDTO.setCreationDate(user.getCreationDate());
        userDTO.setImage(user.getImage());
        userDTO.setTitle(user.getTitle());

        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setLevel(1);
        user.setExperience(0);

        // Assuming reviews, recipes, and awards are managed separately
        List<Review> reviews = reviewRepository.findAllById(userDTO.getReviews_id());
        List<Recipe> recipes = recipeRepository.findAllById(userDTO.getRecipes_id());
        List<Award> awards = awardRepository.findAllById(userDTO.getAwards_id());

        user.setReviews(reviews);
        user.setRecipes(recipes);
        user.setAwards(awards);

        user.setName(userDTO.getName());
        user.setCreationDate(new Date());
        user.setImage(userDTO.getImage());
        user.setTitle("Beginner");

        return user;
    }


}
