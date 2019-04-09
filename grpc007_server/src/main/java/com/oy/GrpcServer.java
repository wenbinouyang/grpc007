package com.oy;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oy.service.grpc.BookGrpc;
import com.oy.service.grpc.UserGrpc;
import com.oy.utils.Utils;

import io.grpc.Server;
import io.grpc.ServerBuilder;

@Component
public class GrpcServer {
	private int port = 23333;
	private Server server;

	@Autowired
	private UserGrpc userGrpc;
	
	@Autowired
	private BookGrpc bookGrpc;

	private void start() throws IOException {
		server = ServerBuilder.forPort(port)
				.addService(userGrpc)
				.addService(bookGrpc)
				.build().start();
		
		Utils.log.info(("grpc service start..."));

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Utils.log.error(("shutting down gRPC server since JVM is shutting down"));
				GrpcServer.this.stop();
				Utils.log.error("gRPC server shut down");
			}
		});
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	// block
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	public void init(String[] args) throws IOException, InterruptedException {
		start();
		blockUntilShutdown();
	}

}