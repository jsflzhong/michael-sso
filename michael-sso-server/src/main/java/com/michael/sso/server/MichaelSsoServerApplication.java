package com.michael.sso.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MichaelSsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MichaelSsoServerApplication.class, args);
    }
}
