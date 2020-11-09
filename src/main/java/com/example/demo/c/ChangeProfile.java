package com.example.demo.c;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangeProfile {
	
	@Autowired
	private Business business;
	
	@Autowired
	private List<Mail> mail;
	
	public void switchToDummy() {
		business.setMail(mail.get(1));
	}

	public void switchToDefault() {
		business.setMail(mail.get(0));
	}


}
