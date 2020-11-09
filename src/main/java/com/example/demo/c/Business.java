package com.example.demo.c;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;

@Service
public class Business {
	
	@Setter
	@Autowired
	private Mail mail;
	
	public void doSomething(String data) {
		mail.send(data);
	}
	
}
