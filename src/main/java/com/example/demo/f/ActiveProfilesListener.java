package com.example.demo.f;

import org.springframework.context.ApplicationContext;

public interface ActiveProfilesListener {

	void setActiveProfiles(ApplicationContext context, String profiles);

	
}
