package com.example.demo.g;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChangeProfileController {
	
	@Autowired
	private ConfigurableEnvironment env;
	
	@Autowired
	private ChangeProfile changeProfile;
	
	@GetMapping(path = "get-active-profiles")
	public String getActiveProfiles() {
		return Arrays.toString(env.getActiveProfiles());
	}

	@GetMapping(path = "change-active-profiles", params = "profiles")
	public String changeActiveProfiles(@RequestParam("profiles") String profiles) {
		String[] split = profiles.split(",");
		env.setActiveProfiles(split);
		changeProfile.setActiveProfiles(profiles);
		return Arrays.toString(env.getActiveProfiles());
	}
}
