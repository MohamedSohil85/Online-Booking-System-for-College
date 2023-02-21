# online-booking-system Project



User stories :<br>
==============================================================
<br>as student i want to register to the app ,and get my Username and Matriculation Number per E-mail
<br>as student i want to log in to the app with Username and my Password
<br>as student i want to get list of Courses(Type of Course, Term, Begin ,End , Duration) ,which i want to select
<br>as student i want to see the Date of Courses and Time
<br>as Student i want to select to Courses with my Matriculation Number
<br>as Student i want to delete my Self from Registration of this Course
<br>as Student i want to see the Examinations (Date ,Time ,Duration,Room)
<br>as Student i want to register to Examination with Matriculation Number
<br>as Student i want to Delete my Self from the Examination
<br>as Student i want to see the Grade of my Examination
<br>as Student i want to get Notification per E-mail ,when the Grade applied 
<br>as Student i want to get List of Grade of my Exams as PDF file
====================================================================
This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory. Be aware that it’s not an _über-jar_ as
the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/online-booking-system-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing JAX-RS and more

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
