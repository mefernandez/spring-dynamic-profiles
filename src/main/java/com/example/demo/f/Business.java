package com.example.demo.f;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Business implements ActiveProfilesListener {
	
	@Autowired
	private Mail mail;
	
	@Autowired
	private Ftp ftp;

	public void doSomething(String data) {
		mail.send(data);
		ftp.send(data);
	}
	
	@Override
	public void setActiveProfiles(ApplicationContext context, String profiles) {
		this.mail = ApplicationContextUtil.findBeanForProfiles(context, profiles, Mail.class);
		this.ftp = ApplicationContextUtil.findBeanForProfiles(context, profiles, Ftp.class);
	}

}
