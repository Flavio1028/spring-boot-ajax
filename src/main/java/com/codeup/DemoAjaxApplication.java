package com.codeup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.codeup.domain.SocialMetaTag;
import com.codeup.service.SocialMetaTagService;

@SpringBootApplication
public class DemoAjaxApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoAjaxApplication.class, args);
	}

	@Autowired
	SocialMetaTagService service;

	@Override
	public void run(String... args) throws Exception {

		SocialMetaTag og = service.getSocialMetaTagByUrl("https://www.udemy.com/course/writing-clean-code/");
		System.out.println(og.toString());
		
		SocialMetaTag og1 = service.getSocialMetaTagByUrl("https://www.pichau.com.br/cadeiras/gamer/cadeira-gamer-razer-tarok-pro-rez-0002");
		System.out.println(og1.toString());
	}

}