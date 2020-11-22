package com.example.demo.g;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles({"mail", "ftp"})
@WebMvcTest(controllers = ChangeProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChangeProfileControllerMvcTest {
	
	@MockBean
	private ChangeProfile changeProfile;
	
	@Autowired
	private MockMvc mvc;

	@Order(0)
	@Test
	void testGetActiveProfiles() throws Exception {
		mvc.perform(get("/get-active-profiles"))
			.andExpect(status().isOk())
			.andExpect(content().string("[ftp, mail]"));
	}

	@Order(1)
	@Test
	void testChangeActiveProfiles() throws Exception {
		mvc.perform(get("/change-active-profiles").param("profiles", "_ftp,_mail"))
			.andExpect(status().isOk())
			.andExpect(content().string("[_ftp, _mail]"));
		verify(changeProfile).setActiveProfiles("_ftp,_mail");
	}
	
}
