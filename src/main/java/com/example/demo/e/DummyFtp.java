package com.example.demo.e;

import org.springframework.stereotype.Component;

import lombok.extern.apachecommons.CommonsLog;

@Component
@CommonsLog
public class DummyFtp implements Ftp {

	@Override
	public void send(String data) {
		log.info("Default FTP send: " + data);
	}

}
