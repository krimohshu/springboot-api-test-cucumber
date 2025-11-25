package com.microservices.order.test.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.cucumber.spring.CucumberContextConfiguration;
import com.microservices.order.OrderServiceApplication;

/**
 * Cucumber Spring configuration
 * This class is required to integrate Cucumber with Spring Boot
 */
@CucumberContextConfiguration
@SpringBootTest(
    classes = OrderServiceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {
    // This class configures the Spring context for Cucumber tests
}
