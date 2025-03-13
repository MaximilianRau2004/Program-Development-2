# Spring Boot RESTful API

> This is an example of a Java RESTful API with Spring Boot and MariaDB.
> It exposes REST resources under http://localhost:8080/api/v1/.
> See the list of resources in the Swagger UI (http://localhost:8080/api/v1/swagger-ui) or OpenAPI documentation as JSON (http://localhost:8080/api/v1/api-docs).

## Prerequisites

- [Install a JDK](https://www.oracle.com/de/java/technologies/downloads/) in at least version 21.
- Make sure that `JAVA_HOME` is set correctly to the root directory of your JDK. You can check with this command: `echo %JAVA_HOME%` (or `echo $JAVA_HOME` on Linux / Git Bash) (or `echo $Env:JAVA_HOME` in Powershell)
- Make sure that the JDK `bin` folder is added to your `PATH`. You can check with this command: `echo %PATH%` (or `echo $PATH` on Linux / Git Bash) (or `echo $Env:PATH` in Powershell)
- You can also install [Maven](https://maven.apache.org/) yourself, but if you use the Maven Wrapper scripts (`mvnw` or `mvnw.cmd`), this is not necessary, since Maven will be downloaded in this case
- Install and start a local [MariaDB server](https://mariadb.org/download/). You can instead use the [docker-compose.yml](../docker-compose.yml). If necessary, update the [API configuration file](src/main/resources/application.properties) with your database port (default is `3306`) and credentials (default is `root`:`root`)

## Usage

```bash
# build and package executable --> appears in target/rest-api.jar
./mvnw clean install

# execute tests only
./mvnw test

# generate test coverage report (execute tests first) --> appears in target/site/jacoco/index.html
./mvnw jacoco:report

# build and package executable without running tests
./mvnw clean install -DskipTests

# run the created JAR file
# --> http://localhost:8080/api/v1/todos
# --> http://localhost:8080/api/v1/assignees
java -jar ./target/rest-api.jar

# for development: build and run in live-reload mode (rebuild on save)
# --> http://localhost:8080/api/v1/todos
# --> http://localhost:8080/api/v1/assignees
./mvnw spring-boot:run
```

After you run the created JAR file, you should be able to see the implemented resources in your browser
(http://localhost:8080/api/v1/assignees) (http://localhost:8080/api/v1/todos)

## Documentation

- General references: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle
- Application properties: https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html
