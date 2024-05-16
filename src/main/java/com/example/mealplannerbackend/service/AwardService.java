package com.example.mealplannerbackend.service;

import com.example.mealplannerbackend.dto.AwardDTO;
import com.example.mealplannerbackend.exceptions.AwardNotFoundException;
import com.example.mealplannerbackend.model.Award;
import com.example.mealplannerbackend.model.User;
import com.example.mealplannerbackend.repository.AwardRepository;
import com.example.mealplannerbackend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwardService {

    private final AwardRepository awardRepository;
    private final UserRepository userRepository;

    public AwardService(AwardRepository awardRepository, UserRepository userRepository) {
        this.awardRepository = awardRepository;
        this.userRepository = userRepository;
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

}
