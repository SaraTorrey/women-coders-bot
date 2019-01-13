package com.saratorrey.womencodersbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class WomenCodersBotApplication {

	public static void main(String[] args) {

		SpringApplication.run(WomenCodersBotApplication.class, args);
	}

	@PostConstruct
	public static void runTwitterBot(){
		System.out.println( "Starting Twitter Bot!" );
		TwitterClient.searchTwitter();
	}
}

