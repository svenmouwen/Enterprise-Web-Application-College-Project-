// Package
package com.application.media.controller;

// Imports

import com.application.media.MainApplicationConfig;
import com.application.media.model.User;
import com.application.media.repository.UserRepository;
import com.application.media.security.JWToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.NotAcceptableStatusException;

import java.util.List;


@CrossOrigin(origins = "${ALLOWED_ORIGINS}")
@RestController
@RequestMapping("/authentication")  
public class AuthenticationController {
    private final UserRepository userRepository;

    private final MainApplicationConfig configuration;

    private User user;

    /**
     * @param userRepository - User repository interface
     * @param configuration  - Configuration class
     */
    public AuthenticationController(UserRepository userRepository, MainApplicationConfig configuration) {
        this.userRepository = userRepository;
        this.configuration = configuration;
    }

    /**
     * @param signInInfo - Object node
     * @return - Returns if the user can log in with his account
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody ObjectNode signInInfo) {
        // Get the input of email and password
        JsonNode emailNode = signInInfo.get("email");
        JsonNode passwordNode = signInInfo.get("password");
        String userEmail = emailNode.asText();
        String userPassword = passwordNode.asText();

        // find the correct account with email
        List<User> users = userRepository.findByAttributeContainsText("email", userEmail);
        user = users.size() == 1 ? users.get(0) : null;

        // Check if the password is verified
        if (user == null || !user.verifyPassword(userPassword)) {
            throw new NotAcceptableStatusException("Cannot authenticate user with email=" + userEmail);
        }

        // Create a new token for the logged in user
        JWToken jwToken = new JWToken(user.getName(), user.getId());
        String tokenString = jwToken.encode(this.configuration.getIssuer(), this.configuration.getPassphrase(), this.configuration.getTokenDurationOfValidity());

        // Returns the response entity
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, tokenString).body(user);
    }
}
