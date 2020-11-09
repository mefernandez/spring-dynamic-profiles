package com.example.demo.e;

import java.beans.Beans;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.util.ContextUtil;
import lombok.Setter;

@Service
public class Business {
	
	@Setter
	@Autowired
	private Mail mail;
	
	@Setter
	@Autowired
	private Ftp ftp;

	public void doSomething(String data) {
		mail.send(data);
		ftp.send(data);
	}
	
	
	public void setActiveProfiles(ApplicationContext context, String profiles) {
		this.mail = ApplicationContextUtil.findBeanForProfiles(context, profiles, Mail.class);
		this.ftp = ApplicationContextUtil.findBeanForProfiles(context, profiles, Ftp.class);
	}


	
}
