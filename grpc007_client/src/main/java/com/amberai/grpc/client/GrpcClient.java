package com.amberai.grpc.client;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import com.amberai.grpc.BookServiceGrpc;
import com.amberai.grpc.GrpcClientPool;
import com.amberai.grpc.GrpcLib.GrpcReply;
import com.amberai.grpc.GrpcLib.addBookRequest;
import com.amberai.grpc.GrpcLib.getUserByIdRequest;
import com.amberai.grpc.UserServiceGrpc;
import com.amberai.utils.UtilFunctions;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GrpcClient {
	public static String host = "localhost";
	private final ManagedChannel channel;
	private final UserServiceGrpc.UserServiceBlockingStub userBlockingStub;
	private final BookServiceGrpc.BookServiceBlockingStub bookBlockingStub;

	public GrpcClient(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		userBlockingStub = UserServiceGrpc.newBlockingStub(channel);
		bookBlockingStub = BookServiceGrpc.newBlockingStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	@SuppressWarnings({ "rawtypes" })
	public static Object call(String rpcMethoddName, Object... args) throws Exception {
		UtilFunctions.log.info("=========== GrpcClient#call begin ===========");
		GrpcClient client = null;
		try {
			client = GrpcClientPool.borrowObject();
			// client = new GrpcClient(host, 23333);

			Class[] argsTypes = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				UtilFunctions.log.info("args types: {}", args[i].getClass());
				argsTypes[i] = args[i].getClass();
			}
			Method method = client.getClass().getMethod(rpcMethoddName, argsTypes);
			Object result = method.invoke(client, args);
			return result;
		} catch (Exception e) {
			UtilFunctions.log.error("GrpcClient#call Exception: {}", e.toString());
			return null;
		} finally {
			if (client != null) {
				GrpcClientPool.returnObject(client);
				// client.shutdown();
			}
		}
	}

	// ============= User module =============
	public Object getUserById(Integer id) {
		UtilFunctions.log.info("=========== GrpcClient#getUserById begin ===========");
		getUserByIdRequest request = getUserByIdRequest.newBuilder().setId(id).build();
		GrpcReply response;
		try {
			response = userBlockingStub.getUserById(request);
			UtilFunctions.log.info("GrpcClient#getUserById response, code:{}, data:{}", response.getCode(),
					response.getData());
		} catch (StatusRuntimeException e) {
			UtilFunctions.log.error("GrpcClient#getUserById error, StatusRuntimeException:{}", e);
			return null;
		}
		return response;
	}

	// ============= Book module =============
	public Object addBook(Integer id, String name, Double price) {
		UtilFunctions.log.info("=========== GrpcClient#addBook begin ===========");
		addBookRequest request = addBookRequest.newBuilder().setId(id).setName(name).setPrice(price).build();
		GrpcReply response;
		try {
			response = bookBlockingStub.addBook(request);
			UtilFunctions.log.info("GrpcClient#addBook response, code:{}, data:{}", response.getCode(),
					response.getData());
		} catch (StatusRuntimeException e) {
			UtilFunctions.log.error("GrpcClient#addBook error, StatusRuntimeException:{}", e);
			return null;
		}
		return response;
	}

}