package com.example.demo.d;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mail")
@SpringBootTest
class BusinessTest {
	
	@Autowired
	private Business business;

	@SpyBean
	private DefaultMail defaultMail;

	@SpyBean
	private DummyMail dummyMail;

	@Autowired
	private ChangeProfile changeProfile;
	
	@Test
	void test() {
		business.doSomething("data");
		verify(defaultMail).send("data");

		changeProfile.setActiveProfiles("_mail");
		business.doSomething("data");
		verify(dummyMail).send("data");

		changeProfile.setActiveProfiles("mail");
		business.doSomething("data");
		verify(defaultMail, times(2)).send("data");
	}

}