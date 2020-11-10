# spring-dynamic-profiles
An essay on how to change Spring active profiles without restarting

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

