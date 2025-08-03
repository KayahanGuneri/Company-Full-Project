package org.example.company_p;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CompanyProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyProjectApplication.class, args);
    }
}


