package com.example.demo.e;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.apachecommons.CommonsLog;

@Profile("ftp")
@Primary
@Component
@CommonsLog
public class DefaultFtp implements Ftp {

	@Override
	public void send(String data) {
		log.info("Dummy FTP send: " + data);
	}

}
