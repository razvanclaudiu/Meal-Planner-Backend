package com.example.mealplannerbackend.controller;

import com.example.mealplannerbackend.dto.AwardDTO;
import com.example.mealplannerbackend.service.AwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/awards")
public class AwardController {

    private final AwardService awardService;

    @Autowired
    public AwardController(AwardService awardService) {
        this.awardService = awardService;
    }

    @GetMapping
    public ResponseEntity<List<AwardDTO>> getAllAwards() {
        List<AwardDTO> awards = awardService.getAllAwards();
        return new ResponseEntity<>(awards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AwardDTO> getAwardById(@PathVariable("id") Long id) {
        AwardDTO awardDTO = awardService.getAwardById(id);
        return new ResponseEntity<>(awardDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AwardDTO> createAward(@RequestBody AwardDTO awardDTO) {
        AwardDTO createdAward = awardService.createAward(awardDTO);
        return new ResponseEntity<>(createdAward, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AwardDTO> updateAward(@PathVariable("id") Long id, @RequestBody AwardDTO awardDTO) {
        AwardDTO updatedAward = awardService.updateAward(id, awardDTO);
        return new ResponseEntity<>(updatedAward, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAward(@PathVariable("id") Long id) {
        awardService.deleteAward(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
