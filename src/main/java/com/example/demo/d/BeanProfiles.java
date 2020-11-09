package com.example.demo.d;

public interface BeanProfiles {
	
	public static final String ANY_BEAN_PROFILE = "*"; 
	
	default String getProfiles() {
		return ANY_BEAN_PROFILE;
	}

}
