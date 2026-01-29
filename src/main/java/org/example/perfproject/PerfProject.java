package org.example.perfproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс - контейнер Spring. Не трогать!
 */
@SpringBootApplication
public class PerfProject {
    public static void main(String[] args){
        SpringApplication.run(PerfProject.class, args);
    }
}
