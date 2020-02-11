# Spring Boot based EV Charging Station.

## Getting Started
* Java 8 is required.
* The project using Spring Boot framework.
* The project is built with Gradle (packaged within the project itself).
* The application runs on port 8080.

## To Launch:
To launch the web server follow instructions as per your case:

### Gradle:
From within project directory run following command  (follow as per your case):
* From git bash, run following command: 

   `./gradle run`
* From windows command prompt: 

  `gradlew run`

### From IntelliJ IDE.
* Gradle is configured to build for IntelliJ. Run following command and import project in IntelliJ:
 <br/>`./gradlew idea`
* Run `Main` from `EvApplication.java`.

## APIs
You can target [http://localhost:8080](http://localhost:8080).

Following webservices are available with example CURL command:

* GET `/chargingSessions`: Gets all session entities:
  
  `curl http://localhost:8080/chargingSessions`

* GET `/chargingSessions/summary`: Get the last minute summary:
  
  `curl http://localhost:8080/chargingSessions/summary`

* POST `/chargingSessions`: This post is with JSON body with station ID for which SessionEntity is to be created.
  
  `curl -d 'JSON_DATA_HERE' -H "Content-Type: application/json" http://localhost:8080/chargingSessions`
  
  Example: `curl -d '{"stationId":"ABC-21"}' -H "Content-Type: application/json" http://localhost:8080/chargingSessions`

* PUT `/chargingSessions/{id}`: Stop the session if already IN_PROGRESS. If the given session is already set to FINISHED, then does nothing and returns the `SessionEntity` as it is.
  
  `curl PUT http://localhost:8080/chargingSessions/{id}`
  
  Example: `curl -X PUT http://localhost:8080/chargingSessions/4f675b93-440b-4349-81b9-f500957291f2`

## Test Coverage:

To luanch the test suite, run following gradle command:

`./gradlew test`

Here is screenshot of the test coverage report:

![alt image](https://github.com/pranav8494/spring-boot-rest-api/blob/master/test_Coverage.JPG)

## Implementation Details:

### BOM
* `SessionEntity`: Abstract POJO for a session entity.
* `ChargingSessionEntity`: Extension of SessionEntity.
* `Summary`: POJO to hold the summary KPIs.
* `StatusEnum`: Enum for possible status for a `SessionEntity`.
* `ChargingStore`: Singleton to keep all the `SessionEntity` in memory.

### Factory:
* `SessionEntityFactory`: Factory to create instances of `SessionEntity` child. 

### API Controller:
* `ChargingSessionContoller`: Controller defining all the available APIs for `SessionController`.

## Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/gradle-plugin/reference/html/)
