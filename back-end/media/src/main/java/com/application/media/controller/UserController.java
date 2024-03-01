// Package
package com.application.media.controller;

// Imports

import com.application.media.exceptions.BadRequestException;
import com.application.media.model.User;
import com.application.media.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "${ALLOWED_ORIGINS}")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * @param user - Create new user
     * @return - Returns the new user
     */
    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getLocation());
        return userRepository.save(user);
    }

    /**
     * @return - Returns all users in the database
     */
    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * @param userId - ID of the user
     * @return - Returns the user find by an ID
     */
    @GetMapping("/{userId}")
    public Optional<User> getUserById(@PathVariable Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * @param userId      - ID of the user
     * @param updatedUser - RequestBody
     * @return - Returns the updated user
     */
    @PutMapping("/update/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        // First find the user by ID
        Optional<User> existingUser = userRepository.findById(userId);

        // Check if the user is present
        if (existingUser.isPresent()) {
            // Get the correct user
            User user = existingUser.get();

            // Change the facts of the user when needed
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setLocation(updatedUser.getLocation());

            // Return the updated user
            return userRepository.save(user);
        } else {
            // No user founded, return nothing
            return null;
        }
    }

    /**
     * @param userId             - ID of the user
     * @param updateUserPassword - RequestBody
     * @return - Returns the updated password of the user
     */
    @PutMapping("/updatePassword/{userId}")
    public User updateUserPassword(@PathVariable Long userId, @RequestBody User updateUserPassword) {
        // First find the user by ID
        Optional<User> existingUserPassword = userRepository.findById(userId);

        // Check if the user is present
        if (existingUserPassword.isPresent()) {
            // get the correct user
            User user = existingUserPassword.get();

            // Change the password of the user when needed
            user.setPassword(updateUserPassword.getPassword());

            // Return the updated user
            return userRepository.save(user);
        } else {
            // No user founded, return nothing
            return null;
        }
    }

    /**
     * @param userId - ID of the user
     * @return - Returns the user that is deleted
     */
    @DeleteMapping("/delete/{userId}")
    public User deleteUser(@PathVariable Long userId) {
        // Find the user by ID
        Optional<User> optionalUser = this.userRepository.findById(userId);

        // Check if the user is present
        if (optionalUser.isEmpty()) {
            throw new BadRequestException("Cannot delete account with id=" + userId);
        }

        // Get the correct user
        User user = optionalUser.get();

        // Delete the user
        this.userRepository.deleteById(userId);

        // Return the user that is deleted
        return user;
    }

    /**
     * @param loginUser - RequestBody
     * @return - Returns of the user is logged in
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginUser) {
        // Find the user by ID
        Optional<User> existingUser = userRepository.findById(loginUser.getId());

        // Check if the user is present and if the user get the correct password
        if (existingUser.isPresent() && existingUser.get().getPassword().equals(loginUser.getPassword())) {
            // Generate a new token
            String token = generateToken(existingUser.get().getId(), existingUser.get().getEmail());

            // Returns the new generated token
            return ResponseEntity.ok(token);
        } else {
            // Invalid login details
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ongeldige inloggegevens");
        }
    }

    /**
     * @param userId    - ID of the user
     * @param userEmail - Email of the user
     * @return - Returns a new generated token
     */
    private String generateToken(Long userId, String userEmail) {
        long expirationTime = 864_000_000; // 10 dagen in milliseconden
        String secret = "jouw_geheime_sleutel"; // Vervang dit door een complexe geheime sleutel

        return Jwts.builder().setSubject(userId.toString()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expirationTime)).claim("email", userEmail).signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}