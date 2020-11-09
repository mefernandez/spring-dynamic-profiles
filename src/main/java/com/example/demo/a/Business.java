package com.example.demo.a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Business {
	
	@Autowired
	private Mail mail;
	
	public void doSomething(String data) {
		mail.send(data);
	}
	
	@Autowired
	private DummyMail dummyMail;
	
	public void switchToDummy() {
		this.mail = this.dummyMail;
	}

	@Autowired
	private DefaultMail defaultMail;

	public void switchToDefault() {
		this.mail = this.defaultMail;
	}
}
