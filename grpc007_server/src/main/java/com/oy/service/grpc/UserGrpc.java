package com.oy.service.grpc;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.oy.grpc.UserServiceGrpc;
import com.oy.grpc.GrpcLib.GrpcReply;
import com.oy.grpc.GrpcLib.getUserByIdRequest;
import com.oy.model.User;
import com.oy.service.UserService;
import com.oy.utils.Utils;

import io.grpc.stub.StreamObserver;

@Component
public class UserGrpc extends UserServiceGrpc.UserServiceImplBase {

	@Resource
	private UserService userService;

	@Override
	public void getUserById(getUserByIdRequest request, StreamObserver<GrpcReply> responseObserver) {
		Utils.log.info("UserGrpc#getUserById, id:{}", request.getId());

		// 调用service层的方法
		User user = userService.getUserById(request.getId());

		String data = JSONObject.toJSONString(user);
		GrpcReply reply = GrpcReply.newBuilder().setCode(0).setData(data).build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

}