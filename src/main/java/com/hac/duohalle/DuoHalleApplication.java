package com.hac.duohalle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DuoHalleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DuoHalleApplication.class, args);
    }

}
