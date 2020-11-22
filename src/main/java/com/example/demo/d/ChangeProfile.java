package com.example.demo.d;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangeProfile implements InitializingBean {
	
	@Autowired
	private Business business;
	
	@Autowired
	private List<BeanProfiles> beans;
	
	private Map<String, BeanProfiles> beansMap;
	
	public void setActiveProfiles(String profiles) {
		BeanProfiles bean = beansMap.get(profiles);
		if (bean != null) {
			business.setMail((Mail) bean);
		} else {
			bean = beansMap.get(BeanProfiles.ANY_BEAN_PROFILE);
			business.setMail((Mail) bean);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (beans == null || beans.isEmpty()) {
			return;
		}
		this.beansMap = beans.stream().collect(
			Collectors.toMap(BeanProfiles::getProfiles, Function.identity())
		);
	}

}
