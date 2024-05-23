package com.example.mealplannerbackend.controller;

import com.example.mealplannerbackend.dto.AuthResponse;
import com.example.mealplannerbackend.dto.LoginDTO;
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
import com.example.mealplannerbackend.service.UserService;
import com.example.mealplannerbackend.utils.JwtAuthEntryPoint;
import com.example.mealplannerbackend.utils.JwtProvider;
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

    private NotificationController notificationController;
    private JwtProvider jwtProvider;



    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository,AwardRepository awardRepository,NotificationRepository notificationRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.awardRepository = awardRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
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
        user.setTitle("Beginner");
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


}
