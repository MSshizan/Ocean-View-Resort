package org.example.ovr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OvrApplication {
    public static void main(String[] args) {
        SpringApplication.run(OvrApplication.class, args);
    }
}
