package com.oy.service.grpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.oy.grpc.BookServiceGrpc;
import com.oy.grpc.GrpcLib.GrpcReply;
import com.oy.grpc.GrpcLib.addBookRequest;
import com.oy.model.Book;
import com.oy.service.BookService;
import com.oy.utils.Utils;

import io.grpc.stub.StreamObserver;

@Component
public class BookGrpc extends BookServiceGrpc.BookServiceImplBase {

	@Autowired
	private BookService bookService;

	@Override
	public void addBook(addBookRequest request, StreamObserver<GrpcReply> responseObserver) {
		Integer id = request.getId();
		String name = request.getName();
		Double price = request.getPrice();
		Utils.log.info("BookGrpc#addBook info, id:{}, name:{}, price:{}", id, name, price);

		// 调用service层的方法
		Book book = new Book();
		book.setId(id);
		book.setName(name);
		book.setPrice(price);
		int result = bookService.addBook(book);

		JSONObject jsonObj = new JSONObject();

		if (result == 1) {
			jsonObj.put("msg", "add book succ");
		} else {
			jsonObj.put("msg", "add book failed");
		}

		String data = jsonObj.toJSONString();
		GrpcReply reply = GrpcReply.newBuilder().setCode(0).setData(data).build();
		responseObserver.onNext(reply);
		// onCompleted() method to specify that we’ve finished dealing with the RPC
		responseObserver.onCompleted();
	}

}
