package com.amberai.grpc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Component;

import com.amberai.grpc.client.GrpcClient;
import com.amberai.utils.UtilFunctions;

@Component
public class GrpcClientPool {

	private GenericObjectPool<GrpcClient> objectPool = null;

	public GrpcClient borrowObject() {
		try {
			GrpcClient client = objectPool.borrowObject();
			UtilFunctions.log.info("=======client:{}, total threads created: " + objectPool.getCreatedCount(), client);
			return client;
		} catch (Exception e) {
			UtilFunctions.log.error("objectPool.borrowObject error, msg:{}, exception:{}", e.toString(), e);
		}
		return createClient();
	}

	public void returnObject(GrpcClient client) {
		try {
			objectPool.returnObject(client);
		} catch (Exception e) {
			UtilFunctions.log.error("objectPool.returnObject error, msg:{}, exception:{}", e.toString(), e);
		}
	}

	private GrpcClient createClient() {
		return new GrpcClient("localhost", 23333);
	}

	public GenericObjectPool<GrpcClient> getObjectPool() {
		return objectPool;
	}

	public void setObjectPool(GenericObjectPool<GrpcClient> objectPool) {
		this.objectPool = objectPool;
	}

	@PostConstruct
	public void init() {
		UtilFunctions.log.info("GrpcClientPool init...");
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		// 池中的最大连接数
		poolConfig.setMaxTotal(8);
		// 最少的空闲连接数
		poolConfig.setMinIdle(0);
		// 最多的空闲连接数
		poolConfig.setMaxIdle(8);
		// 当连接池资源耗尽时,调用者最大阻塞的时间,超时时抛出异常 单位:毫秒数
		poolConfig.setMaxWaitMillis(-1);
		// 连接池存放池化对象方式,true放在空闲队列最前面,false放在空闲队列最后
		poolConfig.setLifo(true);
		// 连接空闲的最小时间,达到此值后空闲连接可能会被移除,默认即为30分钟
		poolConfig.setMinEvictableIdleTimeMillis(1000L * 60L * 30L);
		// 连接耗尽时是否阻塞,默认为true
		poolConfig.setBlockWhenExhausted(true);
		objectPool = new GenericObjectPool<>(new GrpcClientFactory(), poolConfig);
	}

	@PreDestroy
	public void close() {
		if (objectPool != null) {
			UtilFunctions.log.info("GrpcClientPool destroyed...");
			objectPool.close();
		}
	}

}