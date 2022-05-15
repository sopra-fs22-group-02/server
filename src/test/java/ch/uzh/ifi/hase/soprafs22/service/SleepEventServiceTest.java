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
import java.util.Collections;

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
    private Place testPlace;
    private User testUser;
    private User anotherTestUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setUserId(1);

        anotherTestUser = new User();
        anotherTestUser.setUserId(4);

        testPlace = new Place();
        testPlace.setPlaceId(2);
        testPlace.setProviderId(1);
        testPlace.setName("testName");
        testPlace.setAddress("Universitätsstrasse 1");
        testPlace.setClosestCampus(Campus.CENTER);
        testPlace.setDescription("this is my room.");
        testPlace.setPictureOfThePlace("some link");
        testPlace.setSleepEvents(Collections.singletonList(testEvent));

        testEvent = new SleepEvent();
        testEvent.setEventId(3);
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

        // when
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
        Mockito.when(placeRepository.save(Mockito.any())).thenReturn(testPlace);
        Mockito.when(sleepEventRepository.save(Mockito.any())).thenReturn(testEvent);
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);
        Mockito.when(sleepEventRepository.findByEventId(Mockito.anyInt())).thenReturn(testEvent);

    }

    /** problem with for loop in createSleepEvent() in the following 8 tests */

    /*@Test
    public void createSleepEvent_validInputs_success() {
        // when
        SleepEvent createdEvent = sleepEventService.createSleepEvent(testEvent.getProviderId(),testEvent.getPlaceId(), testEvent);

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
        // attempt to create a second event with a (partially) overlapping time frame
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
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(testEvent2.getProviderId(), testEvent2.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case2_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
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
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(testEvent.getProviderId(), testEvent.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case3_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
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
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(testEvent.getProviderId(), testEvent.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case4_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
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
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(testEvent.getProviderId(), testEvent.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case5_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
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
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(testEvent.getProviderId(), testEvent.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case6_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
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
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(testEvent.getProviderId(), testEvent.getPlaceId(), testEvent2));
    }

    @Test
    public void createSleepEvent_overlapWithOtherEvent_case7_throwsException() {
        // attempt to create a second event with a (partially) overlapping time frame
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
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(testEvent.getProviderId(), testEvent.getPlaceId(), testEvent2));
    }*/

    /** SingletonList cannot be returned by findByPlaceId()
     findByPlaceId() should return Place*/
    /*@Test
    public void getAllSleepEventsForPlace_success() {
        // when
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);
        Mockito.when(sleepEventService.getAllSleepEventsForPlace(Mockito.anyInt())).thenReturn(testPlace.getSleepEvents());

        // then
        assertEquals(testEvent, sleepEventService.getAllSleepEventsForPlace(testEvent.getPlaceId()).get(0));
    }*/

    @Test
    public void findSleepEventById_success() {
        // when
        Mockito.when(sleepEventRepository.findByEventId(Mockito.anyInt())).thenReturn(testEvent);

        // then
        assertEquals(testEvent, sleepEventService.findSleepEventById(testEvent.getEventId()));
    }

    @Test
    public void updateSleep_success() {
        // when
        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setEventId(4);
        testEvent2.setProviderId(1);
        testEvent2.setPlaceId(2);
        testEvent2.setApplicants(null);
        testEvent2.setConfirmedApplicant(0);
        testEvent2.setStartDate(LocalDate.of(2024, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2024, 1, 2));
        testEvent2.setStartTime(LocalTime.of(22, 0));
        testEvent2.setEndTime(LocalTime.of(8, 0));
        testEvent2.setState(EventState.AVAILABLE);
        testEvent2.setComment("some comment");
        testEvent2.setApplicationStatus(null);

        Mockito.when(sleepEventRepository.findByEventId(Mockito.anyInt())).thenReturn(testEvent);

        // then
        assertEquals(testEvent, sleepEventService.updateSleepEvent(testEvent.getProviderId(), testEvent.getPlaceId(), testEvent2));
    }

    /** problem with for loop in delete function*/
    /*@Test
    public void deleteSleep_success() {
        // when
        sleepEventService.deleteSleepEvent(testEvent.getEventId(), testEvent.getProviderId());

        // then
        assertNull(sleepEventRepository.findByEventId(testEvent.getEventId()));
    }*/

    /** problem with list.add(int)*/
    /*@Test
    public void addApplicant_success() {
        testEvent.setApplicants(Collections.singletonList(anotherTestUser.getUserId()));
        Mockito.when(testEvent.addApplicant(Mockito.anyInt())).thenReturn(testEvent.getApplicants());
        // when
        SleepEvent updatedEvent = sleepEventService.addApplicant(anotherTestUser.getUserId(), testEvent.getEventId());

        // then
        assertEquals(testEvent.getApplicants(), updatedEvent.getApplicants());
    }*/

    /** problem with for loop */
    /*@Test
    public void confirmSleepEvent_success() {
        // when
        SleepEvent updatedEvent = sleepEventService.confirmSleepEvent(anotherTestUser.getUserId(), testEvent.getEventId());

        // then
        assertEquals(testEvent.getConfirmedApplicant(), updatedEvent.getConfirmedApplicant());
    }*/

    /** problem with for loop in checkIfExpiredOrOver()*/
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

    /** problem with for loop in checkIfExpiredOrOver()*/
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
