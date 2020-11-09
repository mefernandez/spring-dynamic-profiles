package com.example.demo.b;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangeProfile {
	
	@Autowired
	private Business business;
	
	@Autowired
	private DummyMail dummyMail;
	
	public void switchToDummy() {
		business.setMail(this.dummyMail);
	}

	@Autowired
	private DefaultMail defaultMail;

	public void switchToDefault() {
		business.setMail(this.defaultMail);
	}


}
