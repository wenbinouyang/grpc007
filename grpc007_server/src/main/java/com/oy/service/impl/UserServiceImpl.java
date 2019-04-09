package com.oy.service.impl;

import org.springframework.stereotype.Service;

import com.oy.model.User;
import com.oy.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User getUserById(Integer id) {
		User user = null;
		if (id.equals(1)) {
			user = new User();
			user.setAge(25);
			user.setId(1);
			user.setName("张无忌");
			user.setSalary(10000.0);
		} else {
			user = new User();
			user.setAge(20);
			user.setId(2);
			user.setName("周芷若");
			user.setSalary(10000.0);
		}
		return user;
	}

}
