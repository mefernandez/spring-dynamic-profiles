package com.example.demo.g;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest 
class MailProfileNotActiveTest {

	@Autowired
	private ApplicationContext context;
	
	@Test
	void testGetBeansOfType() {
		Map<String, Mail> beansOfType = context.getBeansOfType(Mail.class);
		assertEquals(1, beansOfType.size());
		assertEquals("[dummyMail]", Arrays.toString(beansOfType.keySet().toArray()));
	}

}