package com.realestate.backendrealestate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

@SpringBootApplication
@EnableAsync
public class BackEndRealEstateApplication {

	public static void main(String[] args)  {
		SpringApplication.run(BackEndRealEstateApplication.class, args);
    }

}
