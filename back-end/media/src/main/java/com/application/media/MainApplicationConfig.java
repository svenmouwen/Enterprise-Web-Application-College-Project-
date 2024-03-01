// Package
package com.application.media;

// Imports

import com.application.media.repository.ExtendedRepositoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Public class of a configuration with some propertie
 *
 * @author MilanBouwmeester & others
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.application.media.repository", repositoryBaseClass = ExtendedRepositoryImpl.class)
@EntityScan("com.application.media.model")
@EnableWebMvc
public class MainApplicationConfig implements WebMvcConfigurer {
    @Value("${ALLOWED_ORIGINS}")
    private String allowedOrigins;

    public static final String IP_FORWARDED_FOR = "X-Forwarded-For";

    // JWT configuration that can be adjusted from application.properties
    @Value("${jwt.issuer:private company}")
    private String issuer;

    @Value("${jwt.passphrase:This is very secret information for my private encryption key.}")
    private String passphrase;

    @Value("${jwt.duration-of-validity:1200}") // default 20 minutes;
    private int tokenDurationOfValidity;

    /**
     * @param registry - CorsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**").allowedOriginPatterns(allowedOrigins).allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, IP_FORWARDED_FOR).exposedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, IP_FORWARDED_FOR).allowCredentials(true);
    }

    /**
     * @return - Get the issuer
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * @return - Get the passphrase
     */
    public String getPassphrase() {
        return passphrase;
    }

    /**
     * @return - Get the duration of validity of the toke
     */
    public int getTokenDurationOfValidity() {
        return tokenDurationOfValidity;
    }
}