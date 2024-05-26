package com.example.mealplannerbackend.service;

import com.example.mealplannerbackend.dto.AwardDTO;
import com.example.mealplannerbackend.exceptions.AwardNotFoundException;
import com.example.mealplannerbackend.exceptions.UserNotFoundException;
import com.example.mealplannerbackend.model.*;
import com.example.mealplannerbackend.repository.AwardRepository;
import com.example.mealplannerbackend.repository.CategoryRepository;
import com.example.mealplannerbackend.repository.NotificationRepository;
import com.example.mealplannerbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AwardService {

    private final AwardRepository awardRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final NotificationRepository notificationRepository;

    public AwardService(AwardRepository awardRepository, UserRepository userRepository, CategoryRepository categoryRepository, NotificationRepository notificationRepository) {
        this.awardRepository = awardRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<AwardDTO> getAllAwards() {
        List<Award> awards = awardRepository.findAll();
        return awards.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public AwardDTO getAwardById(Long id) {
        Award award = awardRepository.findById(id)
                .orElseThrow(() -> new AwardNotFoundException("Award not found with id: " + id));
        return convertToDto(award);
    }

    public AwardDTO createAward(AwardDTO awardDTO) {
        Award award = convertToEntity(awardDTO);
        Award savedAward = awardRepository.save(award);
        return convertToDto(savedAward);
    }

    public AwardDTO updateAward(Long id, AwardDTO updatedAwardDTO) {
        // Convert the updated DTO to an entity
        Award updatedAward = convertToEntity(updatedAwardDTO);

        // Retrieve the existing award from the repository
        Award existingAward = awardRepository.findById(id)
                .orElseThrow(() -> new AwardNotFoundException("Award with id " + id + " not found"));

        // Update the fields of the existing award with the new values
        existingAward.setName(updatedAward.getName());
        existingAward.setDescription(updatedAward.getDescription());
        existingAward.setImage(updatedAward.getImage());

        // Save the updated award
        Award savedAward = awardRepository.save(existingAward);

        // Convert the saved award entity back to DTO and return
        return convertToDto(savedAward);
    }

    public void deleteAward(Long id) {
        awardRepository.deleteById(id);
    }

    private AwardDTO convertToDto(Award award) {
        AwardDTO awardDTO = new AwardDTO();
        awardDTO.setId(award.getId());
        awardDTO.setName(award.getName());
        awardDTO.setDescription(award.getDescription());
        awardDTO.setImage(award.getImage());

        // Extracting user IDs from Award's users
        List<Long> userIds = new ArrayList<>();
        for (User user : award.getUsers()) {
            userIds.add(user.getId());
        }
        awardDTO.setUsers_id(userIds);

        return awardDTO;
    }

    private Award convertToEntity(AwardDTO awardDTO) {
        Award award = new Award();
        award.setId(awardDTO.getId());
        award.setName(awardDTO.getName());
        award.setDescription(awardDTO.getDescription());
        award.setImage(awardDTO.getImage());

        // Assuming you have a method to find User by ID
        List<User> users = new ArrayList<>();
        for (Long userId : awardDTO.getUsers_id()) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                users.add(user);
            }
        }
        award.setUsers(users);

        return award;
    }

    // Method to check and award achievements
    public void checkAndAwardAchievements(Long id) {
        User user =  userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        List<Runnable> achievementCheckers = new ArrayList<>();

        // Add achievement checkers only for achievements the user hasn't obtained yet
        if (!user.hasAward("First Taste")) {
            achievementCheckers.add(() -> checkAndAwardFirstTaste(user));
        }
        if (!user.hasAward("Recipe Developer")) {
            achievementCheckers.add(() -> checkAndAwardRecipeDeveloper(user));
        }
        if (!user.hasAward("Culinary Architect")) {
            achievementCheckers.add(() -> checkAndAwardCulinaryArchitect(user));
        }
        if (!user.hasAward("Taste Tester")) {
            achievementCheckers.add(() -> checkAndAwardTasteTester(user));
        }
        if (!user.hasAward("Savory Critic")) {
            achievementCheckers.add(() -> checkAndAwardSavoryCritic(user));
        }
        if (!user.hasAward("Epicurean Evaluator")) {
            achievementCheckers.add(() -> checkAndAwardEpicureanEvaluator(user));
        }
        if (!user.hasAward("Gourmet Judge")) {
            achievementCheckers.add(() -> checkAndAwardGourmetJudge(user));
        }
        if (!user.hasAward("Star Chef")) {
            achievementCheckers.add(() -> checkAndAwardStarChef(user));
        }
        if (!user.hasAward("Master Chef")) {
            achievementCheckers.add(() -> checkAndAwardMasterChef(user));
        }
        if (!user.hasAward("Cooking Enthusiast")) {
            achievementCheckers.add(() -> checkAndAwardCookingEnthusiast(user));
        }
        if (!user.hasAward("Culinary Virtuoso")) {
            achievementCheckers.add(() -> checkAndAwardCulinaryVirtuoso(user));
        }
        if (!user.hasAward("Gastronomy Guru")) {
            achievementCheckers.add(() -> checkAndAwardGastronomyGuru(user));
        }
        if (!user.hasAward("Seasoned Member")) {
            achievementCheckers.add(() -> checkAndAwardSeasonedMember(user));
        }
        if (!user.hasAward("Veteran Cook")) {
            achievementCheckers.add(() -> checkAndAwardVeteranCook(user));
        }
        if (!user.hasAward("Culinary Explorer")) {
            achievementCheckers.add(() -> checkAndAwardCulinaryExplorer(user));
        }

        // Call remaining achievement checkers
        achievementCheckers.forEach(Runnable::run);
        grantLevels(user);

        List<Runnable> achievementCheckersLevel = new ArrayList<>();

        if (!user.hasAward("Cooking Enthusiast")) {
            achievementCheckersLevel.add(() -> checkAndAwardCookingEnthusiast(user));
        }
        if (!user.hasAward("Culinary Virtuoso")) {
            achievementCheckersLevel.add(() -> checkAndAwardCulinaryVirtuoso(user));
        }
        if (!user.hasAward("Gastronomy Guru")) {
            achievementCheckersLevel.add(() -> checkAndAwardGastronomyGuru(user));
        }
        achievementCheckersLevel.forEach(Runnable::run);
        grantLevels(user);
        grantTitles(user);
        userRepository.save(user);
    }

    // Method to check and award "Welcome Aboard" achievement

    // Method to check and award "First Taste" achievement
    public void checkAndAwardFirstTaste(User user) {
        if (!user.getRecipes().isEmpty()) {
            awardAchievement(user, "First Taste");
        }
    }

    // Method to check and award "Recipe Developer" achievement
    public void checkAndAwardRecipeDeveloper(User user) {
        if (user.getRecipes().size() >= 10) {
            awardAchievement(user, "Recipe Developer");
        }
    }

    // Method to check and award "Culinary Architect" achievement
    public void checkAndAwardCulinaryArchitect(User user) {
        if (user.getRecipes().size()  >= 50) {
            awardAchievement(user, "Culinary Architect");
        }
    }

    // Method to check and award "Taste Tester" achievement
    public void checkAndAwardTasteTester(User user) {
        if (!user.getReviews().isEmpty()) {
            awardAchievement(user, "Taste Tester");
        }
    }

    // Method to check and award "Savory Critic" achievement
    public void checkAndAwardSavoryCritic(User user) {
        if (user.getReviews().size() >= 10) {
            awardAchievement(user, "Savory Critic");
        }
    }

    // Method to check and award "Epicurean Evaluator" achievement
    public void checkAndAwardEpicureanEvaluator(User user) {
        if (user.getReviews().size()  >= 50) {
            awardAchievement(user, "Epicurean Evaluator");
        }
    }

    // Method to check and award "Gourmet Judge" achievement
    public void checkAndAwardGourmetJudge(User user) {
        if (user.getReviews().size()  >= 100) {
            awardAchievement(user, "Gourmet Judge");
        }
    }

    // Method to check and award "Star Chef" achievement
    public void checkAndAwardStarChef(User user) {
        for (Recipe recipe : user.getRecipes()) {
            if (recipe.getRating() >= 4) {
                awardAchievement(user, "Star Chef");
                break;
            }
        }
    }

    // Method to check and award "Master Chef" achievement
    public void checkAndAwardMasterChef(User user) {
        for (Recipe recipe : user.getRecipes()) {
            if (recipe.getRating() == 5) {
                awardAchievement(user, "Master Chef");
                break;
            }
        }
    }

    // Method to check and award "Cooking Enthusiast" achievement
    public void checkAndAwardCookingEnthusiast(User user) {
        if (user.getLevel() >= 10) {
            awardAchievement(user, "Cooking Enthusiast");
        }
    }

    // Method to check and award "Culinary Virtuoso" achievement
    public void checkAndAwardCulinaryVirtuoso(User user) {
        if (user.getLevel() >= 50) {
            awardAchievement(user, "Culinary Virtuoso");
        }
    }

    // Method to check and award "Gastronomy Guru" achievement
    public void checkAndAwardGastronomyGuru(User user) {
        if (user.getLevel() >= 100) {
            awardAchievement(user, "Gastronomy Guru");
        }
    }

    // Method to check and award "Seasoned Member" achievement
    public void checkAndAwardSeasonedMember(User user) {
        LocalDate now = LocalDate.now();
        LocalDate creationDate = convertToLocalDate(user.getCreationDate());
        Period period = Period.between(creationDate, now);

        // Checking if the user has been a member for at least 6 months
        if (period.getYears() > 0 || (period.getYears() == 0 && period.getMonths() >= 6)) {
            awardAchievement(user, "Seasoned Member");
        }
    }

    // Method to check and award "Veteran Cook" achievement
    public void checkAndAwardVeteranCook(User user) {
        LocalDate now = LocalDate.now();
        LocalDate creationDate = convertToLocalDate(user.getCreationDate());
        Period period = Period.between(creationDate, now);

        // Check if a full year has passed
        if (period.getYears() >= 1) {
            awardAchievement(user, "Veteran Cook");
        }
    }

    // Method to check and award "Culinary Explorer" achievement
    public void checkAndAwardCulinaryExplorer(User user) {
        List<Category> allCategories = categoryRepository.findAll();
        List<Recipe> userRecipes = user.getRecipes();

        // Collect categories from the user's recipes
        Set<Category> userCategories = userRecipes.stream()
                .flatMap(recipe -> recipe.getCategories().stream())
                .collect(Collectors.toSet());

        // Check if the user has interacted with all categories
        if (userCategories.containsAll(allCategories)) {
            awardAchievement(user, "Culinary Explorer");
        }

    }

    // Method to award achievement to the user
    private void awardAchievement(User user, String achievementName) {
        Award award = awardRepository.getAwardByName(achievementName);
        List<Award> userAwards = user.getAwards();
        userAwards.add(award);
        user.setAwards(userAwards);
        Notification notification = new Notification();
        notification.setUserId(user.getId());
        notification.setAwardId(award.getId());
        notification.setNotificationShown(false);
        notificationRepository.save(notification);
    }

    private void grantLevels(User user) {
        int experience_recipes = user.getRecipes().size() * 70;
        int experience_reviews = user.getReviews().size() * 40;
        int experience_awards = user.getAwards().size() * 200;
        int total_experience = experience_awards + experience_recipes + experience_reviews - 200;
        int total_level = total_experience / 100;
        int remainining_experience = total_experience % 100;
        user.setLevel(total_level + 1);
        user.setExperience(remainining_experience);
    }

    private static final String[] TITLES = {
            "Rookie",      // Levels 1-12
            "Apprentice",  // Levels 13-24
            "Cook",        // Levels 25-36
            "Chef",        // Levels 37-48
            "Gourmet",     // Levels 49-60
            "Connoisseur", // Levels 61-72
            "Virtuoso",    // Levels 73-84
            "Master",      // Levels 85-99
            "Legend"       // Levels 100 and above
    };

    public void grantTitles(User user) {
        int level = user.getLevel();
        String title;

        if (level >= 100) {
            title = "Legend";
        }
        else if(level >=97) {
            title = "Master";
        } else if (level >= 1) {
            int index = (level - 1) / 12;
            if (index >= TITLES.length) {
                index = TITLES.length - 1; // Ensure index is within the array bounds
            }
            title = TITLES[index];
        } else {
            title = "Unknown";
        }

        user.setTitle(title);
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
