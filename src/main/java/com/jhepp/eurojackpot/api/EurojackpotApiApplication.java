package com.jhepp.eurojackpot.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class EurojackpotApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurojackpotApiApplication.class, args);
    }

}
