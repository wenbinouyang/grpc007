package com.amberai;

import javax.annotation.PreDestroy;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.stereotype.Controller;

import com.amberai.grpc.GrpcClientPool;
import com.amberai.grpc.client.GrpcClient;
import com.amberai.utils.UtilFunctions;

@Controller
public class InitController {

	@PreDestroy
	public void destroy() {
		UtilFunctions.log.info("InitController#destroy running...");
		
		GenericObjectPool<GrpcClient> objectPool = GrpcClientPool.getObjectPool();
		UtilFunctions.log.info("InitController#destroy, total threads created: " + objectPool.getCreatedCount());
		
		UtilFunctions.log.info("InitController#destroy: objectPool is closing...");
		objectPool.close();
	}
}
