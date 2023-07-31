package com.venturi.demo;

import com.venturi.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	BookRepository bookRepository;


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


}
