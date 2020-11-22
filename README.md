# spring-dynamic-profiles
An essay* on how to change Spring active profiles without restarting.

(*) Not a real essay!

# Why

I like to use `@Profile` in my Spring applications to include/exclude integration `@Components`.
For instance, I might have a `Mail` interface with a real `DefaultMail` implementation and a `DummyMail`.
It's very useful to run the app at development time and testing use cases without actually sending emails.

Setting active profiles involves setting `spring.active.profiles` (or env var SPRING_ACTIVE_PROFILES)
to a comma-separated list of profiles, and then launching the application.

In all, you need to restart your app upon changing active profiles for it to take effect.

But sometimes, it would be handy to switch profiles without restarting the app.
For instance, if the app is running in production,  it could be handy to switch implementations back and forth.

# How to read this

Before I started to code, I had the feeling Spring should provide a way to accomplish this feature.
I searched for `spring dynamic profiles` and the like, but found no straight answer.

I started off with just a couple of classes and tried to TDD my way out.
I then incrementally added more complexity iteratively.

I kept a package for every iteration, ending with {.a .b .c}
The next section explain the main problem I was trying to solve in each iteration.

# TL;DR

Go to [How to use it](#how-to-use-it).

# The case

![diagram](http://yuml.me/3b1fc7df.png)

The domain is quite simple: `Business` classes wich depend on `Ftp` and `Mail` components (integration).
`Mail` and `Ftp` are interfaces with a real and a dummy implementation each.

# The goal

The goal is also quite simple: Change Ftp and Mail implementations at runtime without restarting the app (nor the Spring context).

# Step by step problem solving

I started to write code and tests inside package `com.example.demo.a` and copied everything into a new package each time I hit a milestone.

```
.
└── com
    └── example
        └── demo
            ├── a
            ├── b
            ├── c
            ├── d
            ├── e
            └── f
```

Here's what's relevant about each iteration.

## package .a : Barebones

This is the first `@Test` stating what I want to achieve.
Tests in later packages repeat this intent.

```java
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
``` 

Here's "the implementation".

```java
@Service
public class Business {
	
	@Autowired
	private Mail mail;
	
	public void doSomething(String data) {
		mail.send(data);
	}
	
	@Autowired
	private DummyMail dummyMail;
	
	public void switchToDummy() {
		this.mail = this.dummyMail;
	}

	@Autowired
	private DefaultMail defaultMail;

	public void switchToDefault() {
		this.mail = this.defaultMail;
	}
}
```

```java
public interface Mail {

	void send(String data);

}
```

```java
@Profile("mail")
@Primary
@CommonsLog
@Component
public class DefaultMail implements Mail {

	@Override
	public void send(String data) {
		log.info("Default Mail send: " + data);
	}

}
```

```java
@CommonsLog
@Component
public class DummyMail implements Mail {

	@Override
	public void send(String data) {
		log.info("Dummy Mail send: " + data);
	}

}
```


Keynotes:
- Small scope, just two main players: Business and Mail.
- There's really no relationship to Spring active profiles
- `@ActiveProfiles("mail")` needs to be there for `DefaultMail` to get loaded by Spring.
- `Business` needs to know all `Mail` implementations.

## package .b : Move it out

The main goal in this iteration was to move all the switching profile code out of `Business`.
I moved it to `ChangeProfile` and kept the implementation as it was.

```java
@Service
public class ChangeProfile {
	
	@Autowired
	private Business business;
	
	@Autowired
	private DummyMail dummyMail;
	
	public void switchToDummy() {
		business.setMail(this.dummyMail);
	}

	@Autowired
	private DefaultMail defaultMail;

	public void switchToDefault() {
		business.setMail(this.defaultMail);
	}

}
```

## package .c: All the impls

Instead of `@Autowiring` each and every `Mail` implementation, 
I used Spring's ability to `@Autowire` a `List` of all the beans that implement `Mail`.

```java
@Service
public class ChangeProfile {
	
	@Autowired
	private Business business;
	
	@Autowired
	private List<Mail> mail;
	
	public void switchToDummy() {
		business.setMail(mail.get(1));
	}

	public void switchToDefault() {
		business.setMail(mail.get(0));
	}
}
```

## package .d: Map Beans to Profile

I tried to implement a logic to switch beans matching currently active profiles
by building a Map associating Beans to Profiles.

```java
	public void setActiveProfiles(String profiles) {
		BeanProfiles bean = beansMap.get(profiles);
		if (bean != null) {
			business.setMail((Mail) bean);
		} else {
			bean = beansMap.get(BeanProfiles.ANY_BEAN_PROFILE);
			business.setMail((Mail) bean);
		}
	}
```

It didn't work. I didn't like it.

## package .e: Back to Business

I changed my mind and moved back the "switch to active profiles" reposibility to Business.
So now `ChangeProfile`'s only mission is to tell every `Business` to switch to these profiles.

```java
@Service
public class ChangeProfile {
	
	@Autowired
	private Business business;
	
	@Autowired
	private ApplicationContext context;
	
	public void setActiveProfiles(String profiles) {
		business.setActiveProfiles(context, profiles);
	}

}
```

I moved the "choose the right beans" code to `ApplicationContextUtil`.
So `Business` is quite clean.

```java
	public void setActiveProfiles(ApplicationContext context, String profiles) {
		this.mail = ApplicationContextUtil.findBeanForProfiles(context, profiles, Mail.class);
		this.ftp = ApplicationContextUtil.findBeanForProfiles(context, profiles, Ftp.class);
	}
```

## package .f: More components, more Business.

Here I introduced `MoreBusiness` and another `Ftp` integration component 
to make this more life-like.

`ChangeProfile` can now handle multiple "Business" classes, which implement `ActiveProfilesListener`.

```java
@Service
public class ChangeProfile {
	
	@Autowired
	private List<ActiveProfilesListener> listeners;
	
	@Autowired
	private ApplicationContext context;
	
	public void setActiveProfiles(String profiles) {
		if (listeners == null || listeners.isEmpty()) {
			return;
		}
		listeners.forEach(x -> x.setActiveProfiles(context, profiles));
	}

}
```
## package .g: `@RestController`.

Here's the missing piece: a `@RestController` with an endpoint to change the current active profiles at runtime.

```java
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
```

Open up a browser at http://localhost:8282/get-active-profiles to get a list of active profiles.
Call http://localhost:8282/change-active-profiles?profiles=a,b to change currently active profiles to `a,b`.

# How to use it

Here's a quick rundown of steps to get dynamic profiles in your project.

1. Copy-paste these classes onto your project (will maybe publish to Maven in the future):
- `ActiveProfilesListener`
- `ApplicationContextUtil`
- `ChangeProfile`
- `ChangeProfileController`
2. Implement `ActiveProfilesListener` in your `Business` classes.
```java
@Service
public class Business implements ActiveProfilesListener {
...
	@Override
	public void setActiveProfiles(ApplicationContext context, String profiles) {
		this.mail = ApplicationContextUtil.findBeanForProfiles(context, profiles, Mail.class);
		this.ftp = ApplicationContextUtil.findBeanForProfiles(context, profiles, Ftp.class);
	}
}
```
3. Done.
