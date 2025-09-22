package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.service.QualifierService;

@SpringBootApplication
public class BfhQualifierApplication implements CommandLineRunner {

	  @Autowired
	    private QualifierService qualifierService;

	    public static void main(String[] args) {
	        SpringApplication.run(BfhQualifierApplication.class, args);
	    }

	    @Override
	    public void run(String... args) throws Exception {
	        qualifierService.executeFlow();
	    }
}
