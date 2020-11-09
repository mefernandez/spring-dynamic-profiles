package com.example.demo.f;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;

public class ApplicationContextUtil {
	
	public static <T> T findBeanForProfiles(ApplicationContext context, String profiles, Class<T> beanType) {
		T bean = null;
		String fallbackBeanName = null;
		String[] beansOfType = context.getBeanNamesForType(beanType);
		for (int i = 0; i < beansOfType.length; i++) {
			String beanName = beansOfType[i];
			Profile profile = context.findAnnotationOnBean(beanName, Profile.class);
			if (profile == null) {
				fallbackBeanName = beanName;
				continue;
			}
			String[] values = profile.value();
			if (Arrays.toString(values).contains(profiles)) {
				bean = context.getBean(beanName, beanType);
				break;
			}
		}
		if (fallbackBeanName != null) {
			bean = context.getBean(fallbackBeanName, beanType);
		}
		return bean;
	}


}
