package com.saratorrey.womencodersbot;

import com.saratorrey.womencodersbot.service.TwitterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@SpringBootApplication
public class WomenCodersBotApplication {

	@Resource
	TwitterService twitterService;

	public static void main(String[] args) {

		SpringApplication.run(WomenCodersBotApplication.class, args);
	}
}

