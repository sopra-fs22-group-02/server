# FindAPlace - SoPra FS22 Group 02 (Backend)
## Introduction
**FindAPlace** is a location-centric event-planner that allows students to quickly and easily find a place to sleep. Whether it is about taking a quick nap, or just crashing at a place to sleep after a long night of studying or partying. **FindAPlace** has you covered!

Places for sleeping are offered by other users. These places can then be chosen when searching for a suitable time slot and location. A simple accept / reject mechanism allows the place provider to then simply choose which applicant will get the place for the desired time.

After being accepted, you or the provider of the place can start a real-time Q&A session where you can ask each other your important questions about the stay.

## Technologies
We used Java and Spring Boot for the backend of the FindAPlace application. To test our application, we use JUnit and Mockito.

## High-level components
Our Backened uses Models, Controllers, Services and REST DTO Mappers. Our main components:

**User:** ([UserController](https://github.com/sopra-fs22-group-02/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/controller/UserController.java), [UserService](https://github.com/sopra-fs22-group-02/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/service/UserService.java)) 
represents a student using the application
- The `User` class and its `UserService` store all the information about the user and contain the user's calendar, where his/her sleep events as provider and applicant are (separately) stored.

**Place:** ([PlaceController](https://github.com/sopra-fs22-group-02/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/controller/PlaceController.java), [PlaceService](https://github.com/sopra-fs22-group-02/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/service/PlaceService.java))
represents the sleep spot a student wants to offer
- A place object contains the information about the place itself (name of the place, provider, address, etc.) as well as a list of the created sleep events.

**Sleep Event:** ([SleepEventController](https://github.com/sopra-fs22-group-02/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/controller/SleepEventController.java), [SleepEventService](https://github.com/sopra-fs22-group-02/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/service/SleepEventService.java))
represents the availability of the place in terms of time slots
- The `SleepEvent` class and its `SleepEventService` handle the application process for a particular time slot.
- A sleep event object is stored in the corresponding place and in the provider's and applicant's calendar.

**Q&A:** ([QnAController](https://github.com/sopra-fs22-group-02/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/controller/QnAController.java),  [WebSocketEventListener](https://github.com/sopra-fs22-group-02/server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/controller/WebSocketEventListener.java))
enables the users to ask each other questions in real-time
- When an applicant is accepted by the provider, the event is approved. As soon as one user starts the Q&A session, the other user is informed via a `Notification` and can join the session. Notifications are used to inform users about the application process (apply, confirm) and Q&A sessions.
- The session is handled via the `WebSocketEventListener`.

## Launch & Deployment
This repository can be downloaded and run via Gradle. Gradle will install all needed dependencies and automatically run tests if the project is being built. Implementing new features will trigger our JUnit tests automatically when rebuilding the project.

### Building with Gradle

You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

#### Build

```bash
./gradlew build
```

#### Run

```bash
./gradlew bootRun
```

#### Test

```bash
./gradlew test
```

### Development Mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## Roadmap
#### Persistent Database
As other features had higher priorities, we're still running on a H2-database that resets all data after restarting the server. As a next step, we want to switch our underlying database management system to PostgreSQL.

#### Authentication and Authorization
As other features had higher priorities and time was of the essence, we did not manage to properly develop an authorization process in the backend. Thereby we want to increase the security of the application.

#### Partial- / Group Booking
In order to extend the booking feature, we want to add a partial booking feature that enables applicant to partially book a sleep event as well as group booking feature which allows a user to apply for a whole group of people.


## Authors and acknowledgment
### Backend
- Marco Thoma
- Johanna Bieri // Group Leader

### Frontend
- Paul Safari
- Dylan Massey
- Yufeng Xiao

### Further Acknowledgment
- Samuel Brügger // TA
- Roy Rutishauser // Assistant
- Thomas Fritz, Prof. Dr.

### License
This project is licensed under the MIT License - see the ([LICENSE.md](https://github.com/sopra-fs22-group-02/server/blob/master/LICENSE.md)) file for details 