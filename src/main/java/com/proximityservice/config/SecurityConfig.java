package com.proximityservice.config;

import com.proximityservice.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:security-config.properties")
public class SecurityConfig {

    @Value("${security.paths.h2}")
    private String h2Paths;

    @Value("${security.paths.swagger}")
    private String swaggerPaths;

    @Value("${security.paths.public}")
    private String publicApiPaths;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(splitPaths(h2Paths)).permitAll()
                .antMatchers(splitPaths(swaggerPaths)).permitAll()
                .antMatchers(splitPaths(publicApiPaths)).permitAll()
                .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .headers().frameOptions().disable();
        return http.build();
    }

    private String[] splitPaths(String paths) {
        return Arrays.stream(paths.split(ApplicationConstants.SECURITY_PROPERTY_DELIMITER))
                .map(String::trim)
                .filter(path -> !path.isEmpty())
                .toArray(String[]::new);
    }
}
