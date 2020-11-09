package com.example.demo.c;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.apachecommons.CommonsLog;

@Profile("mail")
@Primary
@CommonsLog
@Component
public class DefaultMail implements Mail {

	@Override
	public void send(String data) {
		log.info("Default Mail send: " + data);
	}

}
