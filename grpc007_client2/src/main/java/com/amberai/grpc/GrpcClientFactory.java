package com.amberai.grpc;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.amberai.grpc.client.GrpcClient;
import com.amberai.utils.UtilFunctions;

@Component
public class GrpcClientFactory extends BasePooledObjectFactory<GrpcClient> implements ApplicationContextAware {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public GrpcClient create() throws Exception {
		return new GrpcClient("localhost", 23333);
		//return applicationContext.getBean(GrpcClient.class);
	}

	@Override
	public PooledObject<GrpcClient> wrap(GrpcClient client) {
		return new DefaultPooledObject<>(client);
	}

	@Override
	public void destroyObject(PooledObject<GrpcClient> p) throws Exception {
		UtilFunctions.log.info("==== GrpcClientFactory#destroyObject: destroy grpcClient instance ====");
		p.getObject().shutdown();
		super.destroyObject(p);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}