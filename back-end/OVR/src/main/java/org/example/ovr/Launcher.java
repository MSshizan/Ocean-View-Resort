package org.example.ovr;

import javafx.application.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class Launcher {
    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        // Start Spring Boot context
        springContext = new SpringApplicationBuilder(OvrApplication.class).run(args);

        // Launch JavaFX application

    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }
}