package com.example.demo.g;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"mail", "ftp"})
@SpringBootTest
class BusinessTest {
	
	@Autowired
	private Business business;

	@Autowired
	private MoreBusiness moreBusiness;

	@SpyBean
	private DefaultMail defaultMail;

	@SpyBean
	private DummyMail dummyMail;

	@Autowired
	private ChangeProfile changeProfile;
	
	@SpyBean
	private DefaultFtp defaultFtp;

	@SpyBean
	private DummyFtp dummyFtp;

	@DirtiesContext
	@Test
	void testMail() {
		business.doSomething("data");
		verify(defaultMail).send("data");

		changeProfile.setActiveProfiles("_mail");
		business.doSomething("data");
		verify(dummyMail).send("data");

		changeProfile.setActiveProfiles("mail");
		business.doSomething("data");
		verify(defaultMail, times(2)).send("data");

		moreBusiness.doSomething("data");
		verify(defaultMail, times(3)).send("data");

		changeProfile.setActiveProfiles("_mail");
		moreBusiness.doSomething("data");
		verify(dummyMail, times(2)).send("data");

		changeProfile.setActiveProfiles("mail");
		moreBusiness.doSomething("data");
		verify(defaultMail, times(4)).send("data");
	}

	@DirtiesContext
	@Test
	void testFtp() {
		business.doSomething("data");
		verify(defaultFtp).send("data");

		changeProfile.setActiveProfiles("_ftp");
		business.doSomething("data");
		verify(dummyFtp).send("data");

		changeProfile.setActiveProfiles("ftp");
		business.doSomething("data");
		verify(defaultFtp, times(2)).send("data");

		moreBusiness.doSomething("data");
		verify(defaultFtp, times(3)).send("data");

		changeProfile.setActiveProfiles("_ftp");
		moreBusiness.doSomething("data");
		verify(dummyFtp, times(2)).send("data");

		changeProfile.setActiveProfiles("ftp");
		moreBusiness.doSomething("data");
		verify(defaultFtp, times(4)).send("data");
	}
	
}
