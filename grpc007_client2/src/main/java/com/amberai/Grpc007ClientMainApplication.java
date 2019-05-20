package com.amberai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amberai.grpc.GrpcClientPool;
import com.amberai.grpc.client.GrpcClient;
import com.amberai.grpc.client.TestService;
import com.amberai.utils.UtilFunctions;

@SpringBootApplication
public class Grpc007ClientMainApplication implements CommandLineRunner {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Grpc007ClientMainApplication.class, args);
//		TestService.main(args);
	}
	
//	@Bean
//	public GrpcClient GrpcClient(
//			@Value("${grpcClientHostName}") String host, 
//			@Value("${grpcClientHostPort}") int port) {
//		GrpcClient client = new GrpcClient(host, port);
//		return client;
//	}
	
	
//	@Bean(initMethod = "init", destroyMethod = "close")
//	public GrpcClientPool grpcClentPool() {
//		GrpcClientPool grpcClentPool = new GrpcClientPool();
//		return grpcClentPool;
//	}

	@Override
	public void run(String... args) throws Exception {
		
		TestService.main(args);
	}

}
