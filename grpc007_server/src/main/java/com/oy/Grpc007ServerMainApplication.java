package com.oy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Grpc007ServerMainApplication implements CommandLineRunner {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Grpc007ServerMainApplication.class, args);
	}

	@Autowired
	private GrpcServer grpcServer;

	@Override
	public void run(String... args) throws Exception {
		grpcServer.init(args);
	}

}
