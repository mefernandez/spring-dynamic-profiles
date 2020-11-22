package com.example.demo.g;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"mail"})
@SpringBootTest class MailProfileActiveTest {

	@Autowired
	private ApplicationContext context;
	
	@Test
	void testGetBeansOfType() {
		Map<String, Mail> beansOfType = context.getBeansOfType(Mail.class);
		assertEquals(2, beansOfType.size());
		assertEquals("[defaultMail, dummyMail]", Arrays.toString(beansOfType.keySet().toArray()));
	}

}