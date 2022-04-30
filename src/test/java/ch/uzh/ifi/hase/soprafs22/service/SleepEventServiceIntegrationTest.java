package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.ApplicationStatus;
import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.PlaceRepository;
import ch.uzh.ifi.hase.soprafs22.repository.SleepEventRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@WebAppConfiguration
@SpringBootTest
public class SleepEventServiceIntegrationTest {

    @Qualifier("placeRepository")
    @Autowired
    private PlaceRepository placeRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("sleepEventRepository")
    @Autowired
    private SleepEventRepository sleepEventRepository;


    @Autowired
    private PlaceService placeService;

    @Autowired
    private SleepEventService sleepEventService;

    @Autowired
    private UserService userService;

    private User testUser;
    private Place testPlace;
    private SleepEvent sleepEvent;


    @BeforeEach
    public void setup() {
        //fetch all users
        List<User> allUsers = userRepository.findAll();
        // clean every user's calendars
        for(User user : allUsers){
            user.setMyCalendarAsApplicant(Collections.emptyList());
            user.setMyCalendarAsProvider(Collections.emptyList());
        }

        //fetch all places
        List<Place> allPlaces = placeRepository.findAll();
        // clean every place's sleep event list
        for(Place place: allPlaces){
            place.setSleepEvents(Collections.emptyList());
        }

        userRepository.deleteAll();
        placeRepository.deleteAll();
        sleepEventRepository.deleteAll();
    }

    @Test
    public void createPlace_validInputs_success() {
        // given
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 1));
        testEvent.setStartTime(LocalTime.of(8, 0));
        testEvent.setEndTime(LocalTime.of(20, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        // when
        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);


        // then
        assertEquals(testEvent.getEventId(), createdEvent.getEventId());
        assertEquals(testEvent.getPlaceId(), createdEvent.getPlaceId());
        assertEquals(testEvent.getProviderId(), createdEvent.getProviderId());
        assertEquals(testEvent.getApplicants(), createdEvent.getApplicants());
        assertEquals(testEvent.getConfirmedApplicant(), createdEvent.getConfirmedApplicant());
        assertEquals(testEvent.getStartDate(), createdEvent.getStartDate());
        assertEquals(testEvent.getEndDate(), createdEvent.getEndDate());
        assertEquals(testEvent.getStartTime(), createdEvent.getStartTime());
        assertEquals(testEvent.getEndTime(), createdEvent.getEndTime());
        assertEquals(testEvent.getState(), createdEvent.getState());
        assertEquals(testEvent.getComment(), createdEvent.getComment());
        assertEquals(testEvent.getApplicationStatus(), createdEvent.getApplicationStatus());
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case1_throwsException() {
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);

        // attempt to create second place with same user
        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setProviderId(1);
        testEvent2.setPlaceId(2);
        testEvent2.setApplicants(null);
        testEvent2.setConfirmedApplicant(0);
        testEvent2.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2023, 1, 1));
        testEvent2.setStartTime(LocalTime.of(21, 0));
        testEvent2.setEndTime(LocalTime.of(23, 0));
        testEvent2.setState(EventState.AVAILABLE);
        testEvent2.setComment("some comment");
        testEvent2.setApplicationStatus(null);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case2_throwsException() {
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);

        // attempt to create second place with same user
        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setProviderId(1);
        testEvent2.setPlaceId(2);
        testEvent2.setApplicants(null);
        testEvent2.setConfirmedApplicant(0);
        testEvent2.setStartDate(LocalDate.of(2023, 1, 2));
        testEvent2.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent2.setStartTime(LocalTime.of(7, 0));
        testEvent2.setEndTime(LocalTime.of(9, 0));
        testEvent2.setState(EventState.AVAILABLE);
        testEvent2.setComment("some comment");
        testEvent2.setApplicationStatus(null);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case3_throwsException() {
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);

        // attempt to create second place with same user
        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setProviderId(1);
        testEvent2.setPlaceId(2);
        testEvent2.setApplicants(null);
        testEvent2.setConfirmedApplicant(0);
        testEvent2.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent2.setStartTime(LocalTime.of(22, 0));
        testEvent2.setEndTime(LocalTime.of(8, 0));
        testEvent2.setState(EventState.AVAILABLE);
        testEvent2.setComment("some comment");
        testEvent2.setApplicationStatus(null);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case4_throwsException() {
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);

        // attempt to create second place with same user
        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setProviderId(1);
        testEvent2.setPlaceId(2);
        testEvent2.setApplicants(null);
        testEvent2.setConfirmedApplicant(0);
        testEvent2.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent2.setStartTime(LocalTime.of(22, 0));
        testEvent2.setEndTime(LocalTime.of(9, 0));
        testEvent2.setState(EventState.AVAILABLE);
        testEvent2.setComment("some comment");
        testEvent2.setApplicationStatus(null);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case5_throwsException() {
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);

        // attempt to create second place with same user
        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setProviderId(1);
        testEvent2.setPlaceId(2);
        testEvent2.setApplicants(null);
        testEvent2.setConfirmedApplicant(0);
        testEvent2.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent2.setStartTime(LocalTime.of(21, 0));
        testEvent2.setEndTime(LocalTime.of(8, 0));
        testEvent2.setState(EventState.AVAILABLE);
        testEvent2.setComment("some comment");
        testEvent2.setApplicationStatus(null);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case6_throwsException() {
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);

        // attempt to create second place with same user
        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setProviderId(1);
        testEvent2.setPlaceId(2);
        testEvent2.setApplicants(null);
        testEvent2.setConfirmedApplicant(0);
        testEvent2.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent2.setStartTime(LocalTime.of(21, 0));
        testEvent2.setEndTime(LocalTime.of(9, 0));
        testEvent2.setState(EventState.AVAILABLE);
        testEvent2.setComment("some comment");
        testEvent2.setApplicationStatus(null);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case7_throwsException() {
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);

        // attempt to create second place with same user
        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setProviderId(1);
        testEvent2.setPlaceId(2);
        testEvent2.setApplicants(null);
        testEvent2.setConfirmedApplicant(0);
        testEvent2.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent2.setStartTime(LocalTime.of(23, 0));
        testEvent2.setEndTime(LocalTime.of(7, 0));
        testEvent2.setState(EventState.AVAILABLE);
        testEvent2.setComment("some comment");
        testEvent2.setApplicationStatus(null);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent2));
    }


    /*@Test
    public void getAllSleepEventsForPlace_success() {
        // given
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        SleepEvent anotherTestEvent = new SleepEvent();
        anotherTestEvent.setProviderId(1);
        anotherTestEvent.setPlaceId(2);
        anotherTestEvent.setApplicants(null);
        anotherTestEvent.setConfirmedApplicant(0);
        anotherTestEvent.setStartDate(LocalDate.of(2024, 1, 1));
        anotherTestEvent.setEndDate(LocalDate.of(2024, 1, 2));
        anotherTestEvent.setStartTime(LocalTime.of(22, 0));
        anotherTestEvent.setEndTime(LocalTime.of(8, 0));
        anotherTestEvent.setState(EventState.AVAILABLE);
        anotherTestEvent.setComment("some comment");
        anotherTestEvent.setApplicationStatus(null);

        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);
        SleepEvent anotherCreatedEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), anotherTestEvent);
        // shouldn't be necessary! happens in createSleepVent()
        List<SleepEvent> listEvents = new ArrayList<>();
        listEvents.add(testEvent);
        listEvents.add(anotherTestEvent);
        createdPlace.setSleepEvents(listEvents);

        // when
        SleepEvent foundEvent = sleepEventService.getAllSleepEventsForPlace(createdEvent.getPlaceId()).get(0);
        SleepEvent anotherFoundEvent = sleepEventService.getAllSleepEventsForPlace(createdEvent.getPlaceId()).get(1);


        // then
        assertEquals(createdEvent.getEventId(), foundEvent.getEventId());
        assertEquals(anotherCreatedEvent.getEventId(), anotherFoundEvent.getEventId());
    }*/

    @Test
    public void findSleepEventById_success() {
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);

        // when
        SleepEvent foundEvent = sleepEventService.findSleepEventById(createdEvent.getEventId());

        // then
        assertEquals(createdEvent.getEventId(), foundEvent.getEventId());
    }

    @Test
    public void updateSleep_success() {
        // given
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        SleepEvent updates = new SleepEvent();
        updates.setStartDate(LocalDate.of(2024, 2, 2));
        updates.setEndDate(LocalDate.of(2024, 2, 2));
        updates.setStartTime(LocalTime.of(8, 0));
        updates.setEndTime(LocalTime.of(20, 0));
        updates.setComment("some updated comment");

        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);

        // when
        SleepEvent updatedEvent = sleepEventService.updateSleepEvent(createdEvent.getProviderId(), createdEvent.getEventId(), updates);

        // then
        assertEquals(createdEvent.getEventId(), updatedEvent.getEventId());
        assertEquals(updates.getStartDate(), updatedEvent.getStartDate());
        assertEquals(updates.getEndDate(), updatedEvent.getEndDate());
        assertEquals(updates.getStartTime(), updatedEvent.getStartTime());
        assertEquals(updates.getEndTime(), updatedEvent.getEndTime());
    }

    @Test
    public void deleteSleepEvent_success() {
        // given
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User testUser = new User();
        testUser.setUsername("username");
        testUser.setEmail("username@uzh.ch");
        testUser.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User createdUser = userService.createUser(testUser);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdUser.getUserId(), createdPlace.getPlaceId(), testEvent);

        // when
        sleepEventService.deleteSleepEvent(createdEvent.getEventId(), createdEvent.getProviderId());

        // then
        assertNull(sleepEventRepository.findByEventId(createdEvent.getEventId()));
    }

    @Test
    public void addApplicant_success() {
        // given
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User provider = new User();
        provider.setUsername("username");
        provider.setEmail("username@uzh.ch");
        provider.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User applicant = new User();
        applicant.setUsername("username2");
        applicant.setEmail("username2@uzh.ch");
        applicant.setPassword("password2");

        User createdProvider = userService.createUser(provider);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdProvider.getUserId(), createdPlace.getPlaceId(), testEvent);
        User createdApplicant = userService.createUser(applicant);
        // shouldn't be necessary! happens in addApplicant()
        createdApplicant.setMyCalendarAsApplicant(Collections.singletonList(createdEvent));

        // when
        SleepEvent updatedEvent = sleepEventService.addApplicant(createdApplicant.getUserId(), createdEvent.getEventId());

        // then
        assertEquals(createdEvent.getEventId(), updatedEvent.getEventId());
        assertEquals(createdApplicant.getUserId(), updatedEvent.getApplicants().get(0));
        assertEquals(ApplicationStatus.PENDING, updatedEvent.getApplicationStatus());
        assertEquals(createdApplicant.getMyCalendarAsApplicant().get(0).getEventId(), updatedEvent.getEventId());
    }

    @Test
    public void confirmSleepEvent_success() {
        // given
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User provider = new User();
        provider.setUsername("username");
        provider.setEmail("username@uzh.ch");
        provider.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.of(2023, 1, 1));
        testEvent.setEndDate(LocalDate.of(2023, 1, 2));
        testEvent.setStartTime(LocalTime.of(22, 0));
        testEvent.setEndTime(LocalTime.of(8, 0));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User applicant = new User();
        applicant.setUsername("username2");
        applicant.setEmail("username2@uzh.ch");
        applicant.setPassword("password2");

        User createdProvider = userService.createUser(provider);
        Place createdPlace = placeService.createPlace(testPlace);
        User createdApplicant = userService.createUser(applicant);
        testEvent.setApplicants(Collections.singletonList(createdApplicant.getUserId()));
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdProvider.getUserId(), createdPlace.getPlaceId(), testEvent);

        // when
        SleepEvent updatedEvent = sleepEventService.confirmSleepEvent(createdApplicant.getUserId(), createdEvent.getEventId());

        // then
        assertEquals(createdEvent.getEventId(), updatedEvent.getEventId());
        assertEquals(createdApplicant.getUserId(), updatedEvent.getConfirmedApplicant());
        assertEquals(Collections.emptyList(), updatedEvent.getApplicants());
        assertEquals(EventState.UNAVAILABLE, updatedEvent.getState());
        assertEquals(ApplicationStatus.APPROVED, updatedEvent.getApplicationStatus());
    }

    @Test
    public void checkIfExpiredOrOver_deleteEvent() {
        // given
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User provider = new User();
        provider.setUsername("username");
        provider.setEmail("username@uzh.ch");
        provider.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.now());
        testEvent.setEndDate(LocalDate.now());
        testEvent.setStartTime(LocalTime.now().minusHours(1));
        testEvent.setEndTime(LocalTime.now().minusMinutes(1));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User applicant = new User();
        applicant.setUsername("username2");
        applicant.setEmail("username2@uzh.ch");
        applicant.setPassword("password2");

        User createdProvider = userService.createUser(provider);
        Place createdPlace = placeService.createPlace(testPlace);
        User createdApplicant = userService.createUser(applicant);
        testEvent.setApplicants(Collections.singletonList(createdApplicant.getUserId()));
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdProvider.getUserId(), createdPlace.getPlaceId(), testEvent);
        createdApplicant.setMyCalendarAsApplicant(Collections.singletonList(createdEvent));

        // when
        sleepEventService.checkIfExpiredOrOver();

        // then
        assertNull(sleepEventRepository.findByEventId(createdEvent.getEventId()));
    }

    @Test
    public void checkIfExpiredOrOver_setToExpired() {
        // given
        assertNull(userRepository.findByUserId(1));
        assertNull(placeRepository.findByPlaceId(2));
        assertNull(sleepEventRepository.findByEventId(3));

        User provider = new User();
        provider.setUsername("username");
        provider.setEmail("username@uzh.ch");
        provider.setPassword("password");

        Place testPlace = new Place();
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");

        SleepEvent testEvent = new SleepEvent();
        testEvent.setProviderId(1);
        testEvent.setPlaceId(2);
        testEvent.setApplicants(null);
        testEvent.setConfirmedApplicant(0);
        testEvent.setStartDate(LocalDate.now());
        testEvent.setStartTime(LocalTime.now().minusHours(1));
        // to avoid having a start that's before the end, because LocalTime.now() is close to midnight
        // example: LocalDate.now() = 2022-04-30, LocalTime.now() = 23:30, LocalTime.now().plusHours(2) = 01:30
        // --> start: 2022-04-30 23:30, --> end: 2022-04-30 01:30
        LocalDateTime startThisEvent = LocalDateTime.of(LocalDate.now(), LocalTime.now().minusHours(1));
        LocalDateTime endThisEvent = LocalDateTime.of(LocalDate.now(), LocalTime.now().plusHours(2));
        if(endThisEvent.isBefore(startThisEvent)){
            testEvent.setEndDate(LocalDate.now().plusDays(1));
        }
        else{
            testEvent.setEndDate(LocalDate.now());
        }
        testEvent.setEndTime(LocalTime.now().plusHours(2));
        testEvent.setState(EventState.AVAILABLE);
        testEvent.setComment("some comment");
        testEvent.setApplicationStatus(null);

        User createdProvider = userService.createUser(provider);
        Place createdPlace = placeService.createPlace(testPlace);
        SleepEvent createdEvent = sleepEventService.createSleepEvent(createdProvider.getUserId(), createdPlace.getPlaceId(), testEvent);
        createdPlace.setSleepEvents(Collections.singletonList(createdEvent));

        // when
        sleepEventService.checkIfExpiredOrOver();

        // then
        assertEquals(EventState.EXPIRED, sleepEventRepository.findByEventId(createdEvent.getEventId()).getState());
    }
}
