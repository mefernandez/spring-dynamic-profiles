package com.example.demo.e;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ChangeProfile {
	
	@Autowired
	private Business business;
	
	@Autowired
	private ApplicationContext context;
	
	public void setActiveProfiles(String profiles) {
		business.setActiveProfiles(context, profiles);
	}

}
