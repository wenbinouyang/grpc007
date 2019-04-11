package com.amberai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amberai.grpc.client.TestService;

@SpringBootApplication
public class Grpc007ClientMainApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Grpc007ClientMainApplication.class, args);
		TestService.main(args);
	}

}
