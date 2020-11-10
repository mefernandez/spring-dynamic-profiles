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

## .a

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
