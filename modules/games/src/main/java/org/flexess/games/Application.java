package org.flexess.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * Main class for this Spring Boot application.
 *
 * @author stefanz
 */
@SpringBootApplication
@EnableCircuitBreaker
public class Application {

    /**
     * Entry point.
     *
     * @param args command line parameters.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}