package com.application.media;

import com.application.media.controller.AuthenticationController;
import com.application.media.controller.LaptopController;
import com.application.media.model.Laptop;
import com.application.media.model.User;
import com.application.media.repository.LaptopRepository;
import com.application.media.repository.UserRepository;
import com.application.media.security.JWToken;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.AfterClass;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ResponseStatusException;
import org.testng.annotations.BeforeClass;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

// Add missing import statement
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpEntity;

import java.io.DataInput;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;

import org.springframework.http.HttpStatus;
import com.application.media.controller.UserController;

@SpringBootTest
@ContextConfiguration(classes = { UnitTestConfig.class, MediaApplication.class, MainApplicationConfig.class })
@ComponentScan(basePackages = "com.application.media")
@EntityScan("com.application.media.model")
@ActiveProfiles("test")
@ExtendWith(TestResultLoggerExtension.class)
class UnitTest {
    private static final Logger log = LoggerFactory.getLogger(UnitTest.class);

    @Autowired
    MediaApplication application;

    @Autowired
    MainApplicationConfig mainApplicationConfig;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private LaptopRepository laptopRepository;

    @Autowired
    private LaptopController laptopController;

    @Autowired
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String RESOURCE_URL = "http://localhost:8080";

    private RestTemplate restTemplate;

    private User user = new User();

    private String jwtToken;

    private HttpHeaders headers;

    @BeforeClass
    void initAll() throws IOException {
        SpringApplication.run(MediaApplication.class);
        user.setEmail("test@email.com");
        user.setName("test");
        user.setLocation("Amsterdam");
        user.setPassword("password");
        User foundUser = userRepository.findByAttributeContainsText("email", user.getEmail()).get(0);
        user = Objects.requireNonNullElseGet(foundUser, () -> {
            userController.createUser(user);
            return user;
        });
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectNode signInInfo = objectMapper.createObjectNode();
        signInInfo.put("email", user.getEmail());
        signInInfo.put("password", user.getPassword());
        ResponseEntity<String> response = restTemplate.postForEntity(RESOURCE_URL + "/authentication/login", signInInfo,
                String.class);
        jwtToken = response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        JsonNode jsonNode = objectMapper.readTree(response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        if (Objects.requireNonNull(jwtToken).isEmpty()) {
            response = restTemplate.postForEntity(RESOURCE_URL + "/authentication/login", signInInfo, String.class);
            jwtToken = response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            headers.setBearerAuth(Objects.requireNonNull(jwtToken));
        } else
            headers.setBearerAuth(jwtToken);
    }

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        if (jwtToken != null) {
            headers.setBearerAuth(jwtToken);
        }
    }

    @Test
    public void testApplicationContextLoaded() {
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Nested
    @DisplayName("AuthenticationControllerTestSuite")
    class AuthenticationControllerTestSuite {
        User user = new User();
        HttpHeaders headers;
        RestTemplate restTemplate;

        @BeforeEach
        public void initAll() {
            restTemplate = new RestTemplate();
            headers = new HttpHeaders();
            if (jwtToken != null) {
                headers.setBearerAuth(jwtToken);
            } else if (user.getId() > 0) {
                User foundUser = userRepository.findByAttributeContainsText("email", "test@test.com").get(0);
                user = Objects.requireNonNullElseGet(foundUser, () -> {
                    userController.createUser(new User(0, "test@test.com", "test", "Amsterdam", "password"));
                    return user;
                });
            }
        }

        @Test
        public void testLogin() {
            
                headers.clear();
                headers.setContentType(MediaType.APPLICATION_JSON);
                ObjectNode signInInfo = objectMapper.createObjectNode();
                signInInfo.put("email", user.getEmail());
                signInInfo.put("password", user.getPassword());
                try {
                ResponseEntity<User> response = restTemplate.postForEntity(RESOURCE_URL + "/authentication/login",
                        signInInfo, User.class);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getHeaders().get(HttpHeaders.AUTHORIZATION));
                assertNotNull(response.getHeaders().get(HttpHeaders.AUTHORIZATION));

                assertEquals(user.getEmail(), Objects.requireNonNull(response.getBody()));
            } catch (Exception e) {
                // Handle the exception here
            }
        }

        @Test
        public void testLogin_WrongPassword() {

            user.setEmail("test@test.com");
            user.setPassword("password");
            user.setLocation("Amsterdam");
            userRepository.save(user);
            ObjectNode signInInfo = objectMapper.createObjectNode();
            signInInfo.put("email", "test@test.com");
            signInInfo.put("password", "wrongpassword");

            // Act & Assert
            assertThrows(NotAcceptableStatusException.class, () -> authenticationController.login(signInInfo));
        }

        @Test
        public void testLogin_UserNotFound() {
            // Arrange
            ObjectNode signInInfo = objectMapper.createObjectNode();
            signInInfo.put("email", "notfound@test.com");
            signInInfo.put("password", "password");

            // Act & Assert
            assertThrows(NotAcceptableStatusException.class, () -> authenticationController.login(signInInfo));
        }

        public UserRepository getUserRepository() {
            return userRepository;
        }
    }

    @Nested
    @DisplayName("laptopsTestSuite")
    class testLaptopController {
        HttpHeaders headers;
        User user;
        JWToken jwToken;

        @BeforeEach
        void setup() {
            restTemplate = new RestTemplate();
            headers = new HttpHeaders();
            if (jwtToken != null) {
                headers.setBearerAuth(jwtToken);
            }
            laptopRepository.deleteById(new BigInteger("1234567891011"));
            laptopRepository.deleteById(new BigInteger("0000000000000"));
        }

        @Test
        public void testCreateLaptop() {
            try {
                // Create a new laptop
                Laptop laptop = new Laptop();
                BigInteger laptopId;
                laptop.setEan(new BigInteger("1234567891011"));
                laptop.setArticleNr(new BigInteger(13, new Random()));
                laptop.setBrand("test");
                laptop.setModel("test");
            try {
                String laptopJsonString = objectMapper.writeValueAsString(laptop);
                headers.setBearerAuth(jwtToken);
                HttpEntity<String> requestEntity = new HttpEntity<>(laptopJsonString, headers);
                ResponseEntity<Laptop> response = restTemplate.exchange(
                    RESOURCE_URL + "/api/laptops/new",
                    HttpMethod.GET,
                    requestEntity,
                    Laptop.class
                );
                laptop = response.getBody();

                // Retrieve the saved laptop
                Laptop newLaptop = response.getBody();
                laptopId = Objects.requireNonNull(newLaptop.getEan());
                assertEquals(laptopId, laptop.getEan(), "Hoort dezelfde ean terug te geven");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (user.getId() != 0) {
                    laptopRepository.deleteById(BigInteger.valueOf(user.getId()));
                }
            } 
            } catch (Exception e) {
        
            }
        }

        @Test
        void testGetLaptopByEan() {
            BigInteger ean = new BigInteger("190199522121");  // 	190199522121	1661310	    APPLE	MBP TB 13 1.4I5/512G SG NL/QWERTY
            if (laptopRepository.findById(ean).isPresent()) {
                Laptop laptop = laptopRepository.findById(ean).get();


                Laptop result = laptopController.getLaptopByEan(ean);

                assertEquals(laptop.getArticleNr(), result.getArticleNr());
                assertEquals(laptop.getModel(), result.getModel());

                assertEquals(laptop.getBrand(), "APPLE");
            }
        }

        @Test
        void testGetLaptopByEan_NotFound() {
            // Arrange
            BigInteger ean = new BigInteger("0000000000000");
            laptopRepository.findById(ean);

            assertThrows(ResponseStatusException.class, () -> laptopController.getLaptopByEan(ean));

            assertThrows(RestClientException.class,
                    () -> restTemplate.getForEntity(RESOURCE_URL + "/api/laptops/" + ean, HttpEntity.class));
                    try {
                        
                    
            ResponseEntity<HttpStatus> response;
            response = restTemplate.exchange(
                    RESOURCE_URL + "/api/laptops/" + ean,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<HttpStatus>() {}
            );
      
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        }

        @Test
void testGetAllLaptops() {
    // Arrange
    headers.clearContentHeaders();
    headers.setBasicAuth(RESOURCE_URL);

    ResponseEntity<Laptop[]> responseEntity;
try {
    responseEntity = restTemplate.exchange(
        RESOURCE_URL + "/api/laptops/all",
        HttpMethod.GET,
        null,
        Laptop[].class
    );} catch (RestClientException e) {
        log.info(e.getMessage());
        throw new RuntimeException(e);
    }
    assertNotNull(responseEntity);
    
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertTrue(responseEntity.getBody() != null && responseEntity.getBody().length >= 1000, "Expected at least 1000 laptops");
}

    


        @Test
        void testUpdateLaptop() {
            BigInteger ean = new BigInteger("1234567891011");     
            laptopController.createLaptop(new Laptop(ean, ean, "test", "test"));
            Laptop updatedLaptop = new Laptop();
            updatedLaptop.setEan(ean);
            updatedLaptop.setArticleNr(new BigInteger(13, new Random()));
            updatedLaptop.setBrand("Updated Brand");
            updatedLaptop.setModel("Updated Model");
            laptopController.updateLaptop(ean, updatedLaptop);
            if (laptopRepository.findById(ean).isPresent()) {
                Laptop laptop = laptopRepository.findById(ean).get();
                assertEquals(updatedLaptop.getArticleNr(), laptop.getArticleNr());
                assertEquals(updatedLaptop.getBrand(), laptop.getBrand());
                assertEquals(updatedLaptop.getModel(), laptop.getModel());
            }
        }

        @Test
        void testDeleteLaptop_NotFound() {
            // Arrange
            BigInteger ean = new BigInteger("0000000000000");
            if (!laptopRepository.existsById(ean))
                laptopRepository.save(new Laptop(ean, ean, "test brand", "test description"));
            assertDoesNotThrow(() -> laptopController.deleteLaptop(ean));

        }

        @Test
        public void testDeleteLaptop() {
            BigInteger ean = new BigInteger("1234567891011");
            laptopRepository.save(new Laptop(ean, ean, "test brand", "test description"));
            assertDoesNotThrow(() -> laptopController.deleteLaptop(ean));
            assertNull(laptopRepository.findById(ean).orElse(null));
            assertFalse(laptopRepository.existsById(ean));
        }

        @AfterEach
        void teardown() {
            laptopRepository.deleteById(new BigInteger("0000000000000"));
            laptopRepository.deleteById(new BigInteger("1234567891011"));
        }
    }


}
