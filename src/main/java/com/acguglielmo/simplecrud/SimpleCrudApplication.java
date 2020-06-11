package com.acguglielmo.simplecrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("com.acguglielmo.simplecrud")
public class SimpleCrudApplication {

	public static void main(String[] args) {

	    SpringApplication.run(SimpleCrudApplication.class, args);

	}

}
