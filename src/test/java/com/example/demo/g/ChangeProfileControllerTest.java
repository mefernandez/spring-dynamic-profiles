package com.example.demo.g;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles({"mail", "ftp"})
@ExtendWith(SpringExtension.class)
@Import(ChangeProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChangeProfileControllerTest {
	
	@Autowired
	private ChangeProfileController controller;
	
	@Autowired
	private ConfigurableEnvironment env;
	
	@MockBean
	private ChangeProfile changeProfile;

	@Order(0)
	@Test
	void testGetActiveProfiles() {
		assertEquals("[ftp, mail]", Arrays.toString(env.getActiveProfiles()));
		assertEquals("[ftp, mail]", controller.getActiveProfiles());
	}

	@Order(1)
	@Test
	void testChangeActiveProfiles() {
		assertEquals("[_ftp, _mail]", controller.changeActiveProfiles("_ftp,_mail"));
		assertEquals("[_ftp, _mail]", Arrays.toString(env.getActiveProfiles()));
		verify(changeProfile).setActiveProfiles("_ftp,_mail");
	}
}
