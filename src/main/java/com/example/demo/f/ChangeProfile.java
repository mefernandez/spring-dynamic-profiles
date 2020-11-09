package com.example.demo.f;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ChangeProfile {
	
	@Autowired
	private List<ActiveProfilesListener> listeners;
	
	@Autowired
	private ApplicationContext context;
	
	public void setActiveProfiles(String profiles) {
		if (listeners == null || listeners.isEmpty()) {
			return;
		}
		listeners.forEach(x -> x.setActiveProfiles(context, profiles));
	}

}
