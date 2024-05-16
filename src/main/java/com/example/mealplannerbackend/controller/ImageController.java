package com.example.mealplannerbackend.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final String recipeImageDirectory = "images_recipe";
    private final String userImageDirectory = "images_user";

    @GetMapping("/recipe/{imageName}")
    public ResponseEntity<Resource> getRecipeImage(@PathVariable String imageName) throws IOException {
        return getImage(recipeImageDirectory, imageName);
    }

    @GetMapping("/user/{imageName}")
    public ResponseEntity<Resource> getUserImage(@PathVariable String imageName) throws IOException {
        return getImage(userImageDirectory, imageName);
    }

    @PostMapping("/recipe/upload")
    public ResponseEntity<String> uploadRecipeImage(@RequestBody ImageUploadRequest request) {
        return uploadImage(recipeImageDirectory, request.getImageData(), request.getRecipeId());
    }

    @PostMapping("/user/upload")
    public ResponseEntity<String> uploadUserImage(@RequestBody String imageData) {
        return uploadImage(userImageDirectory, imageData, null); // No ID for user images
    }

    // Define a class to represent the request body
    public static class ImageUploadRequest {
        private String imageData;
        private Long recipeId;

        // Getters and setters
        public String getImageData() {
            return imageData;
        }

        public void setImageData(String imageData) {
            this.imageData = imageData;
        }

        public Long getRecipeId() {
            return recipeId;
        }

        public void setRecipeId(Long recipeId) {
            this.recipeId = recipeId;
        }
    }

    private ResponseEntity<Resource> getImage(String directory, String imageName) throws IOException {
        Path imagePath = Paths.get(directory).resolve(imageName);
        Resource resource = new UrlResource(imagePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<String> uploadImage(String directory, String imageData, Long recipeId) {
        if (imageData == null || imageData.isEmpty()) {
            return new ResponseEntity<>("Please provide an image", HttpStatus.BAD_REQUEST);
        }

        try {
            int commaIndex = imageData.indexOf(",");
            if (commaIndex == -1) {
                return new ResponseEntity<>("Invalid image data", HttpStatus.BAD_REQUEST);
            }

            String base64Image = imageData.substring(commaIndex + 1);

            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);

            // Generate a filename based on the recipe ID
            String fileName;
            if (recipeId != null) {
                fileName = "recipe_" + recipeId + ".jpeg";
            } else {
                fileName = "uploaded_image.jpeg"; // For user images
            }
            Path imagePath = Paths.get(directory).resolve(fileName);
            Files.write(imagePath, imageBytes);

            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
