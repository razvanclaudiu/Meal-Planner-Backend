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
    private final String reviewImageDirectory = "images_reviews";

    @GetMapping("/recipe/{imageName}")
    public ResponseEntity<Resource> getRecipeImage(@PathVariable String imageName) throws IOException {
        return getImage(recipeImageDirectory, imageName);
    }

    @GetMapping("/user/{imageName}")
    public ResponseEntity<Resource> getUserImage(@PathVariable String imageName) throws IOException {
        return getImage(userImageDirectory, imageName);
    }

    @GetMapping("/review/{imageName}")
    public ResponseEntity<Resource> getReviewImage(@PathVariable String imageName) throws IOException {
        return getImage(reviewImageDirectory, imageName);
    }

    @PostMapping("/recipe/upload")
    public ResponseEntity<String> uploadRecipeImage(@RequestBody ImageUploadRequest request) {
        return uploadImage(recipeImageDirectory, request.getImageData(), request.getId(), "recipe");
    }

    @PostMapping("/user/upload")
    public ResponseEntity<String> uploadUserImage(@RequestBody ImageUploadRequest request) {
        return uploadImage(userImageDirectory, request.getImageData(), request.getId(), "user"); // No ID for user images
    }

    @PostMapping("/review/upload")
    public ResponseEntity<String> uploadReviewImage(@RequestBody ImageUploadRequest request) {
        return uploadImage(reviewImageDirectory, request.getImageData(), request.getId(), "review");
    }

    public static class ImageUploadRequest {
        private String imageData;
        private String id;

        public String getImageData() {
            return imageData;
        }

        public void setImageData(String imageData) {
            this.imageData = imageData;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

    private ResponseEntity<String> uploadImage(String directory, String imageData, String entityId, String entityType) {
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

            String fileName;
            if ("user".equalsIgnoreCase(entityType)) {
                fileName = "user_" + entityId + ".jpeg";
            } else if ("recipe".equalsIgnoreCase(entityType)) {
                fileName = "recipe_" + entityId + ".jpeg";
            } else if ("review".equalsIgnoreCase(entityType)) {
                fileName = "review_" + entityId + ".jpeg";
            } else {
                fileName = "uploaded_image.jpeg"; // Default for unknown entity types
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
