package com.phatnt15.noticemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NoticeManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoticeManagementApplication.class, args);
	}

}
