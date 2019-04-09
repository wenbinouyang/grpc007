package com.amberai.grpc;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.amberai.grpc.client.GrpcClient;
import com.amberai.utils.UtilFunctions;

public class GrpcClientPool {

	private static GenericObjectPool<GrpcClient> objectPool = null;

	static {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(8);
		poolConfig.setMinIdle(0);
		poolConfig.setMaxIdle(8);
		poolConfig.setMaxWaitMillis(-1);
		poolConfig.setLifo(true);
		poolConfig.setMinEvictableIdleTimeMillis(1000L * 60L * 30L);
		poolConfig.setBlockWhenExhausted(true);
		objectPool = new GenericObjectPool<>(new GrpcClientFactory(), poolConfig);
	}

	public static GrpcClient borrowObject() {
		try {
			GrpcClient client = objectPool.borrowObject();
			UtilFunctions.log.info("=============total threads created: " + objectPool.getCreatedCount());
			return client;
		} catch (Exception e) {
			UtilFunctions.log.error("objectPool.borrowObject Exception:{}", e);
		}
		return createClient();
	}

	public static void returnObject(GrpcClient client) {
		try {
			objectPool.returnObject(client);
		} catch (Exception e) {
			UtilFunctions.log.error("objectPool.borrowObject Exception:{}", e);
		}
	}

	private static GrpcClient createClient() {
		return new GrpcClient("localhost", 23333);
	}

}