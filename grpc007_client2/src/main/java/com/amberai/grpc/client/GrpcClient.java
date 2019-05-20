package com.amberai.grpc.client;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

@Component
public class GrpcClient {
	private final ManagedChannel channel;
	private final UserServiceGrpc.UserServiceBlockingStub userBlockingStub;
	private final BookServiceGrpc.BookServiceBlockingStub bookBlockingStub;

	private static GrpcClientPool grpcClientPool;
	
	@Autowired
	public void setGrpcClientPool(GrpcClientPool grpcClientPool) {
		GrpcClient.grpcClientPool = grpcClientPool;
	}

	//public GrpcClient(String host, int port) {
	public GrpcClient(@Value("${grpcClientHostName}") String host, 
			@Value("${grpcClientHostPort}") int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		userBlockingStub = UserServiceGrpc.newBlockingStub(channel);
		bookBlockingStub = BookServiceGrpc.newBlockingStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
	}

	@SuppressWarnings({ "rawtypes" })
	public static Object call(String rpcMethoddName, Object... args) throws Exception {
		UtilFunctions.log.info("=========== GrpcClient#call begin");
		
		GrpcClient client = null;
		try {
			client = grpcClientPool.borrowObject();
			UtilFunctions.log.info("=========== grpcClientPool:{}, client:{} ===========", grpcClientPool, client);
			

			Class[] argsTypes = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				UtilFunctions.log.info("args types: {}", args[i].getClass());
				argsTypes[i] = args[i].getClass();
			}
			Method method = client.getClass().getMethod(rpcMethoddName, argsTypes);
			Object result = method.invoke(client, args);
			UtilFunctions.log.info("=========== GrpcClient#call end ===========");
			return result;
		} catch (Exception e) {
			UtilFunctions.log.error("GrpcClient#call error, msg:{}, exception:{}", e.toString(), e);
			return null;
		} finally {
			if (client != null) {
				grpcClientPool.returnObject(client);
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
			UtilFunctions.log.error("GrpcClient#addBook error, msg:{}, exception:{}", e.toString(), e);
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
			UtilFunctions.log.info("=========== GrpcClient#addBook end ===========");
		} catch (StatusRuntimeException e) {
			UtilFunctions.log.error("GrpcClient#addBook error, msg:{}, exception:{}", e.toString(), e);
			return null;
		}
		return response;
	}

}