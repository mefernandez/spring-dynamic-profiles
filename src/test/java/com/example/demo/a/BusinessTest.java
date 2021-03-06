package com.example.demo.a;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
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

	@Test
	void test() {
		business.doSomething("data");
		verify(defaultMail).send("data");

		business.switchToDummy();
		business.doSomething("data");
		verify(dummyMail).send("data");

		business.switchToDefault();
		business.doSomething("data");
		verify(defaultMail, times(2)).send("data");
	}

}
