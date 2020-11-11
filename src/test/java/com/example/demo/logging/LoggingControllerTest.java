package com.example.demo.logging;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoggingControllerTest {
	
	@Autowired
	private LoggingController controller;
	
	@Autowired
	private MockMvc mvc;

	@Order(0)
	@Test
	void testInitialLevels() {
		assertTrue(controller.isErrorEnabled());
		assertTrue(controller.isWarnEnabled());
		assertTrue(controller.isInfoEnabled());
		assertFalse(controller.isDebugEnabled());
		assertFalse(controller.isTraceEnabled());
	}
	
	@Order(1)
	@Test
	void testLogWithInitialLogLevels() throws Exception {
		mvc.perform(get("/log"))
			.andExpect(status().isOk())
			.andExpect(content().string("See the log for details"));
	}

	@Order(2)
	@Test
	void testChangeLogLevelToTraceAndLog() throws Exception {
		mvc.perform(
				post("/actuator/loggers/com.example.demo.logging")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"configuredLevel\": \"TRACE\"}")
			)
			.andExpect(status().isNoContent());
		
		mvc.perform(get("/log"))
			.andExpect(status().isOk())
			.andExpect(content().string("See the log for details"));
		
		assertTrue(controller.isErrorEnabled());
		assertTrue(controller.isWarnEnabled());
		assertTrue(controller.isInfoEnabled());
		assertTrue(controller.isDebugEnabled());
		assertTrue(controller.isTraceEnabled());
	}
}
