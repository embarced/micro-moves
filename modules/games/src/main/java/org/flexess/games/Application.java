package org.flexess.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for this Spring Boot application.
 *
 * @author stefanz
 */
@SpringBootApplication
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