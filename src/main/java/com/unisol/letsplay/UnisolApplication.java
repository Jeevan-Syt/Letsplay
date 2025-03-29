package com.unisol.letsplay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.unisol.letsplay.repository")
public class UnisolApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnisolApplication.class, args);
    }
}