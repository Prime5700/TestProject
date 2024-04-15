package com.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Base64;

@SpringBootApplication
public class TestProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestProjectApplication.class, args);
//        System.out.println(Base64.getEncoder().encodeToString(RandomStringUtils.random(30).getBytes()));
    }
}


