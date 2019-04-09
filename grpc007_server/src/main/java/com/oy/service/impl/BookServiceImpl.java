package com.oy.service.impl;

import org.springframework.stereotype.Service;

import com.oy.model.Book;
import com.oy.service.BookService;

@Service
public class BookServiceImpl implements BookService{

	@Override
	public int addBook(Book book) {
		return 1;
	}

}
