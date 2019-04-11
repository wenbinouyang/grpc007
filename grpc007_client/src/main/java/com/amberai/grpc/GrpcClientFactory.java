package com.amberai.grpc;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.amberai.grpc.client.GrpcClient;
import com.amberai.utils.UtilFunctions;

public class GrpcClientFactory extends BasePooledObjectFactory<GrpcClient> {

	@Override
	public GrpcClient create() throws Exception {
		return new GrpcClient("localhost", 23333);
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

}