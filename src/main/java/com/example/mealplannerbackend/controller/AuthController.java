package com.example.mealplannerbackend.controller;

import com.example.mealplannerbackend.dto.AuthResponse;
import com.example.mealplannerbackend.dto.LoginDTO;
import com.example.mealplannerbackend.dto.OAuth2LoginDTO;
import com.example.mealplannerbackend.dto.RegisterDTO;
import com.example.mealplannerbackend.exceptions.UserNotFoundException;
import com.example.mealplannerbackend.model.Award;
import com.example.mealplannerbackend.model.Notification;
import com.example.mealplannerbackend.model.Role;
import com.example.mealplannerbackend.model.User;
import com.example.mealplannerbackend.repository.AwardRepository;
import com.example.mealplannerbackend.repository.NotificationRepository;
import com.example.mealplannerbackend.repository.RoleRepository;
import com.example.mealplannerbackend.repository.UserRepository;
import com.example.mealplannerbackend.service.EmailService;
import com.example.mealplannerbackend.service.GoogleTokenVerifier;
import com.example.mealplannerbackend.service.UserService;
import com.example.mealplannerbackend.utils.JwtAuthEntryPoint;
import com.example.mealplannerbackend.utils.JwtProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AwardRepository awardRepository;
    private NotificationRepository notificationRepository;
    private PasswordEncoder passwordEncoder;

    private JwtProvider jwtProvider;

    private final GoogleTokenVerifier googleTokenVerifier;

    private EmailService emailService;

    @Autowired
    private ImageController imageController; // Inject ImageController

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, AwardRepository awardRepository, NotificationRepository notificationRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, GoogleTokenVerifier googleTokenVerifier, ImageController imageController, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.awardRepository = awardRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.googleTokenVerifier = googleTokenVerifier;
        this.imageController = imageController;
        this.emailService = emailService;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO){
        if(userRepository.existsByUsername(registerDTO.getUsername())){
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setName(registerDTO.getName());
        user.setCreationDate(new Date());
        user.setExperience(0);
        user.setLevel(1);
        user.setTitle("Rookie");
        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));
        user.setImage("user_"+user.getUsername()+".jpeg");
        Award award = awardRepository.getAwardByName("Welcome Aboard");
        List<Award> userAwards = new ArrayList<>();
        userAwards.add(award);
        user.setAwards(userAwards);

        User savedUser = userRepository.save(user);

        Notification notification = new Notification();
        notification.setUserId(savedUser.getId());
        notification.setAwardId(award.getId());
        notification.setNotificationShown(false);
        notificationRepository.save(notification);

        return new ResponseEntity<>("User registered succes!", HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    @PostMapping("oauth2/login")
    public ResponseEntity<AuthResponse> oauth2Login(@RequestBody OAuth2LoginDTO oauth2LoginDTO) {
        try {
            GoogleIdToken.Payload payload = googleTokenVerifier.verify(oauth2LoginDTO.getToken());
            if (payload == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String email = payload.getEmail();
            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> registerNewUser(email, payload));

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), "google"));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtProvider.generateToken(authentication);
            return new ResponseEntity<>(new AuthResponse(token,user.getUsername()), HttpStatus.OK);
        } catch (GeneralSecurityException | IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User registerNewUser(String email, GoogleIdToken.Payload payload) {
        User user = new User();

        String username = email.replace("@gmail.com", "");

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("google"));
        user.setName((String) payload.get("name"));
        user.setEmail(email);
        user.setCreationDate(new Date());
        user.setExperience(0);
        user.setLevel(1);
        user.setTitle("Rookie");
        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));
        user.setImage("user_" + username + ".jpeg");

        Award award = awardRepository.getAwardByName("Welcome Aboard");
        List<Award> userAwards = new ArrayList<>();
        userAwards.add(award);
        user.setAwards(userAwards);

        User savedUser = userRepository.save(user);

        Notification notification = new Notification();
        notification.setUserId(savedUser.getId());
        notification.setAwardId(award.getId());
        notification.setNotificationShown(false);
        notificationRepository.save(notification);

        emailService.sendWelcomeEmail(user);

        return savedUser;
    }


}
