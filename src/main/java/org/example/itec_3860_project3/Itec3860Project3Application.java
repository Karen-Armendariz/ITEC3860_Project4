package org.example.itec_3860_project3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication

@EnableScheduling
public class Itec3860Project3Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Itec3860Project3Application.class, args);
    }
}