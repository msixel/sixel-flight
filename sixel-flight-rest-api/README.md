# Flight finder Rest interface

Rest api interface written in java spring

# Getting Started

This is a spring boot project packaged as JAR file, used to expose the core functions as REST interface.

# Building

In order to generate a new tested build and install in the local maven repository run in terminal the following

```
$ mvn clean install
```

# Running

In order to run the project in standalone mode run in terminal the following

```
$ mvn spring-boot:run
```

# APIs

The following REST APIs are available;

```
$ curl --location --request GET 'http://localhost:8080/routes/GRU-ORL'
$ curl --location --request GET 'http://localhost:8080/routes/GRU-ORL/best'
$ curl --location --request POST 'http://localhost:8080/routes/add' --header 'Content-Type: application/json' \
     --data-raw '{"route": { "from": { "code": "ZZZ" }, "to": { "code": "XXX" } }, "price": 155 }'
```