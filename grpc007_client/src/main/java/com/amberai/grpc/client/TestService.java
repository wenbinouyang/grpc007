package com.amberai.grpc.client;

import com.amberai.grpc.GrpcLib.GrpcReply;
import com.amberai.utils.UtilFunctions;

public class TestService {

	public static void main(String[] args) throws Exception {

		for (int i = 0; i < 1; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					GrpcReply result = null;
					try {
						//result = (GrpcReply) GrpcClient.call("getUserById", Integer.valueOf("1"));
						//result = (GrpcReply) GrpcClient.call("getUserById", 2);
						result = (GrpcReply) GrpcClient.call("addBook", 1, "thinking in java", 50.0);
					} catch (Exception e) {
						e.printStackTrace();
					}
					UtilFunctions.log.info("client call interface, get code:{}, data:{}", result.getCode(),
							result.getData());

				}
			}).start();
		}

	}
}
