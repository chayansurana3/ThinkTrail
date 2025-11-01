package com.thinktrail.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThinktrailApplication {
	public static void main(String[] args) {
		System.out.println("🚀 Starting ThinkTrail backend...");
		SpringApplication.run(ThinktrailApplication.class, args);
	}
}