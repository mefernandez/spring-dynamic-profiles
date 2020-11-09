package com.example.demo.d;

import org.springframework.stereotype.Component;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Component
public class DummyMail implements Mail {

	@Override
	public void send(String data) {
		log.info("Dummy Mail send: " + data);
	}

}
