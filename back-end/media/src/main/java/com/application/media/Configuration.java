package com.application.media;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@org.springframework.context.annotation.Configuration
@EnableWebMvc
public class Configuration implements WebMvcConfigurer {
    @Value("${ALLOWED ORIGINS")
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
        registry.addMapping("/")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, IP_FORWARDED_FOR)
                .exposedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, IP_FORWARDED_FOR)
                .allowCredentials(true);
    }

    public String getIssuer() {
        return issuer;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public int getTokenDurationOfValidity() {
        return tokenDurationOfValidity;
    }
}
