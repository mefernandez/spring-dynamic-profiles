package com.example.demo.g;

import org.springframework.context.ApplicationContext;

public interface ActiveProfilesListener {

	void setActiveProfiles(ApplicationContext context, String profiles);

	
}
