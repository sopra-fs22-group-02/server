package ch.uzh.ifi.hase.soprafs22.service;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SleepEventServiceTest {

    @Mock
    private SleepEventRepository sleepEventRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SleepEventService sleepEventService;

    @InjectMocks
    private PlaceService placeService;

    @InjectMocks
    private UserService userService;

    private SleepEvent testEvent;
    private SleepEvent pastEvent;
    private SleepEvent expiredEvent;
    private User testUser;
    private Place testPlace;
    private List<SleepEvent> events;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setUserId(1);

        testPlace = new Place();
        testPlace.setPlaceId(2);
        testPlace.setProviderId(1);
        testPlace.setSleepEvents(Collections.singletonList(testEvent));

        testEvent = new SleepEvent();
        testEvent.setEventId(10);
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
        anotherTestEvent.setEventId(11);
        anotherTestEvent.setProviderId(1);
        anotherTestEvent.setPlaceId(2);
        anotherTestEvent.setApplicants(null);
        anotherTestEvent.setConfirmedApplicant(0);
        anotherTestEvent.setStartDate(LocalDate.of(2023, 1, 13));
        anotherTestEvent.setEndDate(LocalDate.of(2023, 1, 14));
        anotherTestEvent.setStartTime(LocalTime.of(22, 0));
        anotherTestEvent.setEndTime(LocalTime.of(8, 0));
        anotherTestEvent.setState(EventState.AVAILABLE);
        anotherTestEvent.setComment("some other comment");
        anotherTestEvent.setApplicationStatus(null);

        events = new ArrayList<>();
        events.add(testEvent);
        events.add(anotherTestEvent);
        testUser.setMyCalendarAsProvider(events);
        testPlace.setSleepEvents(events);

        // when
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
        Mockito.when(placeRepository.save(Mockito.any())).thenReturn(testPlace);
        Mockito.when(userRepository.findByUserId(Mockito.anyInt())).thenReturn(testUser);
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);
        Mockito.when(sleepEventRepository.findByEventId(Mockito.anyInt())).thenReturn(testEvent);

    }

    /** problem with for loop in createSleepEvent() in the following 8 tests */

    @Test
    public void createSleepEvent_validInputs_success() {
        // given
        SleepEvent newEvent = new SleepEvent();
        newEvent.setEventId(12);
        newEvent.setStartDate(LocalDate.of(2022, 12, 13));
        newEvent.setEndDate(LocalDate.of(2022, 12, 14));
        newEvent.setStartTime(LocalTime.of(22, 0));
        newEvent.setEndTime(LocalTime.of(8, 0));
        newEvent.setComment("some other comment");

        Mockito.when(sleepEventRepository.save(Mockito.any())).thenReturn(newEvent);
        // when
        SleepEvent createdEvent = sleepEventService.createSleepEvent(testUser.getUserId(), testPlace.getPlaceId(), newEvent);

        assertEquals(newEvent.getEventId(), createdEvent.getEventId());
        assertEquals(testPlace.getPlaceId(), createdEvent.getPlaceId());
        assertEquals(testUser.getUserId(), createdEvent.getProviderId());
        assertEquals(newEvent.getStartDate(), createdEvent.getStartDate());
        assertEquals(newEvent.getEndDate(), createdEvent.getEndDate());
        assertEquals(newEvent.getStartTime(), createdEvent.getStartTime());
        assertEquals(newEvent.getEndTime(), createdEvent.getEndTime());
        assertEquals(EventState.AVAILABLE, createdEvent.getState());
        assertEquals(newEvent.getComment(), createdEvent.getComment());
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case1_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
        SleepEvent overlappingEvent = new SleepEvent();
        overlappingEvent.setEventId(13);
        overlappingEvent.setProviderId(1);
        overlappingEvent.setPlaceId(2);
        overlappingEvent.setStartDate(LocalDate.of(2023, 1, 1));
        overlappingEvent.setEndDate(LocalDate.of(2023, 1, 1));
        overlappingEvent.setStartTime(LocalTime.of(21, 0));
        overlappingEvent.setEndTime(LocalTime.of(23, 0));
        overlappingEvent.setComment("some comment");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(overlappingEvent.getProviderId(), overlappingEvent.getPlaceId(), overlappingEvent));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case2_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
        SleepEvent overlappingEvent = new SleepEvent();
        overlappingEvent.setEventId(13);
        overlappingEvent.setProviderId(1);
        overlappingEvent.setPlaceId(2);
        overlappingEvent.setStartDate(LocalDate.of(2023, 1, 2));
        overlappingEvent.setEndDate(LocalDate.of(2023, 1, 2));
        overlappingEvent.setStartTime(LocalTime.of(7, 0));
        overlappingEvent.setEndTime(LocalTime.of(9, 0));
        overlappingEvent.setComment("some comment");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(overlappingEvent.getProviderId(), overlappingEvent.getPlaceId(), overlappingEvent));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case3_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
        SleepEvent overlappingEvent = new SleepEvent();
        overlappingEvent.setEventId(13);
        overlappingEvent.setProviderId(1);
        overlappingEvent.setPlaceId(2);
        overlappingEvent.setStartDate(LocalDate.of(2023, 1, 1));
        overlappingEvent.setEndDate(LocalDate.of(2023, 1, 2));
        overlappingEvent.setStartTime(LocalTime.of(22, 0));
        overlappingEvent.setEndTime(LocalTime.of(8, 0));
        overlappingEvent.setState(EventState.AVAILABLE);
        overlappingEvent.setComment("some comment");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(overlappingEvent.getProviderId(), overlappingEvent.getPlaceId(), overlappingEvent));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case4_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
        SleepEvent overlappingEvent = new SleepEvent();
        overlappingEvent.setEventId(13);
        overlappingEvent.setProviderId(1);
        overlappingEvent.setPlaceId(2);
        overlappingEvent.setStartDate(LocalDate.of(2023, 1, 1));
        overlappingEvent.setEndDate(LocalDate.of(2023, 1, 2));
        overlappingEvent.setStartTime(LocalTime.of(22, 0));
        overlappingEvent.setEndTime(LocalTime.of(9, 0));
        overlappingEvent.setComment("some comment");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(overlappingEvent.getProviderId(), overlappingEvent.getPlaceId(), overlappingEvent));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case5_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
        SleepEvent overlappingEvent = new SleepEvent();
        overlappingEvent.setEventId(13);
        overlappingEvent.setProviderId(1);
        overlappingEvent.setPlaceId(2);
        overlappingEvent.setStartDate(LocalDate.of(2023, 1, 1));
        overlappingEvent.setEndDate(LocalDate.of(2023, 1, 2));
        overlappingEvent.setStartTime(LocalTime.of(21, 0));
        overlappingEvent.setEndTime(LocalTime.of(8, 0));
        overlappingEvent.setComment("some comment");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(overlappingEvent.getProviderId(), overlappingEvent.getPlaceId(), overlappingEvent));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case6_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
        SleepEvent overlappingEvent = new SleepEvent();
        overlappingEvent.setEventId(13);
        overlappingEvent.setProviderId(1);
        overlappingEvent.setPlaceId(2);
        overlappingEvent.setStartDate(LocalDate.of(2023, 1, 1));
        overlappingEvent.setEndDate(LocalDate.of(2023, 1, 2));
        overlappingEvent.setStartTime(LocalTime.of(21, 0));
        overlappingEvent.setEndTime(LocalTime.of(9, 0));
        overlappingEvent.setComment("some comment");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(overlappingEvent.getProviderId(), overlappingEvent.getPlaceId(), overlappingEvent));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case7_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
        SleepEvent overlappingEvent = new SleepEvent();
        overlappingEvent.setEventId(13);
        overlappingEvent.setProviderId(1);
        overlappingEvent.setPlaceId(2);
        overlappingEvent.setStartDate(LocalDate.of(2023, 1, 1));
        overlappingEvent.setEndDate(LocalDate.of(2023, 1, 2));
        overlappingEvent.setStartTime(LocalTime.of(23, 0));
        overlappingEvent.setEndTime(LocalTime.of(7, 0));
        overlappingEvent.setComment("some comment");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(overlappingEvent.getProviderId(), overlappingEvent.getPlaceId(), overlappingEvent));
    }

    //problem: SingletonList cannot be returned by findByPlaceId(), findByPlaceId() should return Place
    // solution: not mock function that is being tested, not compare lists of objects, but the ids of the objects
    @Test
    public void getAllSleepEventsForPlace_success() {
        // when
        List<SleepEvent> returnedEvents = sleepEventService.getAllSleepEventsForPlace(testPlace.getPlaceId());

        // then
        assertEquals(testPlace.getSleepEvents().get(0).getEventId(), returnedEvents.get(0).getEventId());
        assertEquals(testPlace.getSleepEvents().get(1).getEventId(), returnedEvents.get(1).getEventId());
    }

    @Test
    public void findSleepEventById_success() {
        // when
        Mockito.when(sleepEventRepository.findByEventId(Mockito.anyInt())).thenReturn(testEvent);

        // then
        assertEquals(testEvent, sleepEventService.findSleepEventById(testEvent.getEventId()));
    }

    @Test
    public void updateSleep_success() {
        // given
        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setStartDate(LocalDate.of(2024, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2024, 1, 2));
        testEvent2.setStartTime(LocalTime.of(22, 0));
        testEvent2.setEndTime(LocalTime.of(8, 0));
        testEvent2.setComment("some new comment");

        // when
        SleepEvent updatedEvent = sleepEventService.updateSleepEvent(testEvent.getProviderId(), testEvent.getPlaceId(), testEvent2);

        // then
        assertEquals(testEvent.getEventId(), updatedEvent.getEventId());
        assertEquals(testEvent.getPlaceId(), updatedEvent.getPlaceId());
        assertEquals(testEvent.getProviderId(), updatedEvent.getProviderId());
        assertEquals(testEvent2.getStartDate(), updatedEvent.getStartDate());
        assertEquals(testEvent2.getEndDate(), updatedEvent.getEndDate());
        assertEquals(testEvent2.getStartTime(), updatedEvent.getStartTime());
        assertEquals(testEvent2.getEndTime(), updatedEvent.getEndTime());
        assertEquals(testEvent2.getComment(), updatedEvent.getComment());
    }

    // problem with mock of findByEventId()
    /*@Test
    public void deleteSleep_success() {
        // when
        sleepEventService.deleteSleepEvent(testEvent.getEventId(), testEvent.getProviderId());

        // then
        assertNull(sleepEventRepository.findByEventId(testEvent.getEventId()));
    }*/

    // problem with list.add(int)
    /*@Test
    public void addApplicant_success() {
        User testUser2 = new User();
        testUser2.setUserId(5);

        User testUser3 = new User();
        testUser3.setUserId(6);

        List<Integer> listApplicants = new ArrayList<>();
        listApplicants.add(testUser2.getUserId());

        testUser3.setMyCalendarAsApplicant(events);

        testEvent.setApplicants(listApplicants);
        //Mockito.when(testEvent.addApplicant(Mockito.anyInt())).thenReturn(testEvent.getApplicants());
        // when
        SleepEvent updatedEvent = sleepEventService.addApplicant(testUser3.getUserId(), testEvent.getEventId());

        // then
        assertEquals(testEvent.getApplicants().get(0), updatedEvent.getApplicants().get(0));
        assertEquals(testEvent.getApplicants().get(1), updatedEvent.getApplicants().get(1));
    }*/

    // problem with for loop
    /*@Test
    public void confirmSleepEvent_success() {
        // when
        SleepEvent updatedEvent = sleepEventService.confirmSleepEvent(anotherTestUser.getUserId(), testEvent.getEventId());

        // then
        assertEquals(testEvent.getConfirmedApplicant(), updatedEvent.getConfirmedApplicant());
    }*/

    // problem with for loop in checkIfExpiredOrOver()
    /*@Test
    public void checkIfExpiredOrOver_deleteEvent() {
        pastEvent = new SleepEvent();
        pastEvent.setEventId(5);
        pastEvent.setProviderId(1);
        pastEvent.setPlaceId(2);
        pastEvent.setApplicants(null);
        pastEvent.setConfirmedApplicant(0);
        pastEvent.setStartDate(LocalDate.now());
        pastEvent.setEndDate(LocalDate.now());
        pastEvent.setStartTime(LocalTime.now().minusHours(1));
        pastEvent.setEndTime(LocalTime.now().minusMinutes(1));
        pastEvent.setState(EventState.AVAILABLE);
        pastEvent.setComment("some comment");
        pastEvent.setApplicationStatus(null);

        Mockito.when(placeRepository.findAll()).thenReturn(Collections.singletonList(testPlace));
        // when
        sleepEventService.checkIfExpiredOrOver();

        // then
        assertNull(sleepEventRepository.findByEventId(pastEvent.getEventId()));
    }*/

    // problem with for loop in checkIfExpiredOrOver()
    /*@Test
    public void checkIfExpiredOrOver_setToExpired() {
        expiredEvent = new SleepEvent();
        expiredEvent.setEventId(5);
        expiredEvent.setProviderId(1);
        expiredEvent.setPlaceId(2);
        expiredEvent.setApplicants(null);
        expiredEvent.setConfirmedApplicant(0);
        expiredEvent.setStartDate(LocalDate.now());
        expiredEvent.setEndDate(LocalDate.now());
        expiredEvent.setStartTime(LocalTime.now().minusMinutes(1));
        expiredEvent.setEndTime(LocalTime.now().plusHours(1));
        expiredEvent.setState(EventState.AVAILABLE);
        expiredEvent.setComment("some comment");
        expiredEvent.setApplicationStatus(null);

        Mockito.when(placeRepository.findAll()).thenReturn(Collections.singletonList(testPlace));
        // when
        sleepEventService.checkIfExpiredOrOver();

        // then
        assertEquals(EventState.EXPIRED, expiredEvent.getState());
    }*/
}
