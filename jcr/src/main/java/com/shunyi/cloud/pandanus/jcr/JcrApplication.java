package com.shunyi.cloud.pandanus.jcr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class JcrApplication {

	public static void main(String[] args) {
		String testInfo = "Pandanus startup";
		log.info("The test info is :{}", testInfo);
		SpringApplication.run(JcrApplication.class, args);
	}

}
