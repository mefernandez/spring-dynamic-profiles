package com.example.demo.f;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mail")
@SpringBootTest
class ApplicationContextTest {
	
	@Autowired
	private ApplicationContext context;

	@Test
	void testGetBeansWithAnnotation() {
		Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(Profile.class);
		assertEquals(1, beansWithAnnotation.size());
		assertEquals("[defaultMail]", Arrays.toString(beansWithAnnotation.keySet().toArray()));
	}
	
	@Test
	void testGetBeanNamesForType() {
		String[] beanNamesForType = context.getBeanNamesForType(Mail.class);
		assertEquals(2, beanNamesForType.length);
		assertEquals("[defaultMail, dummyMail]", Arrays.toString(beanNamesForType));
		Profile profile = context.findAnnotationOnBean(beanNamesForType[0], Profile.class);
		assertEquals("[mail]", Arrays.toString(profile.value()));
	}



}
