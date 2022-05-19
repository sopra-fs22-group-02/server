package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.ApplicationStatus;
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

    private SleepEvent testEvent;
    private SleepEvent anotherTestEvent;
    private Place testPlace;
    private User testUser;
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

        anotherTestEvent = new SleepEvent();
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
        Mockito.when(sleepEventRepository.findByEventId(Mockito.anyInt())).thenReturn(testEvent);

    }


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
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);
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
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);

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
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);

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
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);

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
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);

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
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);

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
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);

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
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);

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

    @Test
    public void createSleepEvent_tooShort() {
        // given
        SleepEvent newEvent = new SleepEvent();
        newEvent.setEventId(12);
        newEvent.setStartDate(LocalDate.of(2022, 11, 1));
        newEvent.setEndDate(LocalDate.of(2022, 11, 1));
        newEvent.setStartTime(LocalTime.of(22, 0));
        newEvent.setEndTime(LocalTime.of(22, 30));
        newEvent.setComment("some comment");

        Mockito.when(sleepEventRepository.save(Mockito.any())).thenReturn(newEvent);
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);

        // when
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(testUser.getUserId(), testPlace.getPlaceId(), newEvent));
    }

    @Test
    public void createSleepEvent_tooLong() {
        // given
        SleepEvent newEvent = new SleepEvent();
        newEvent.setEventId(12);
        newEvent.setStartDate(LocalDate.of(2022, 11, 1));
        newEvent.setEndDate(LocalDate.of(2022, 11, 2));
        newEvent.setStartTime(LocalTime.of(22, 0));
        newEvent.setEndTime(LocalTime.of(11, 0));
        newEvent.setComment("some comment");

        Mockito.when(sleepEventRepository.save(Mockito.any())).thenReturn(newEvent);
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);

        // when
        assertThrows(ResponseStatusException.class, () -> sleepEventService.createSleepEvent(testUser.getUserId(), testPlace.getPlaceId(), newEvent));
    }


    @Test
    public void getAllSleepEventsForPlace_success() {
        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);

        // when
        List<SleepEvent> returnedEvents = sleepEventService.getAllSleepEventsForPlace(testPlace.getPlaceId());

        // then
        assertEquals(testPlace.getSleepEvents().get(0).getEventId(), returnedEvents.get(0).getEventId());
        assertEquals(testPlace.getSleepEvents().get(1).getEventId(), returnedEvents.get(1).getEventId());
    }

    @Test
    public void getAllSleepEventsForPlace_placeDoesNotExist() {
        assertThrows(ResponseStatusException.class, () -> sleepEventService.getAllSleepEventsForPlace(20));
    }

    @Test
    public void getAllAvailableSleepEventsForPlace_success() {
        User testUser2 = new User();
        testUser2.setUserId(5);

        SleepEvent unavailableEvent = new SleepEvent();
        unavailableEvent.setEventId(12);
        unavailableEvent.setProviderId(1);
        unavailableEvent.setPlaceId(2);
        unavailableEvent.setApplicants(null);
        unavailableEvent.setConfirmedApplicant(5);
        unavailableEvent.setStartDate(LocalDate.of(2023, 1, 13));
        unavailableEvent.setEndDate(LocalDate.of(2023, 1, 14));
        unavailableEvent.setStartTime(LocalTime.of(22, 0));
        unavailableEvent.setEndTime(LocalTime.of(8, 0));
        unavailableEvent.setState(EventState.UNAVAILABLE);
        unavailableEvent.setComment("some other comment");
        unavailableEvent.setApplicationStatus(ApplicationStatus.APPROVED);

        events.add(unavailableEvent);
        testPlace.setSleepEvents(events);

        Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);

        // when
        List<SleepEvent> returnedEvents = sleepEventService.getAllAvailableSleepEventsForPlace(testPlace.getPlaceId());

        // then
        assertEquals(2, returnedEvents.size());
        assertEquals(testEvent.getEventId(), returnedEvents.get(0).getEventId());
        assertEquals(anotherTestEvent.getEventId(), returnedEvents.get(1).getEventId());
    }

    @Test
    public void getAllAvailableSleepEventsForPlace_placeDoesNotExist() {
        assertThrows(ResponseStatusException.class, () -> sleepEventService.getAllAvailableSleepEventsForPlace(20));
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

    @Test
    public void updateSleep_alreadyApproved() {
        // given
        User testUser2 = new User();
        testUser2.setUserId(5);

        SleepEvent eventToBeUpdated = new SleepEvent();
        eventToBeUpdated.setEventId(20);
        eventToBeUpdated.setProviderId(1);
        eventToBeUpdated.setPlaceId(2);
        eventToBeUpdated.setApplicants(Collections.emptyList());
        eventToBeUpdated.setConfirmedApplicant(5);
        eventToBeUpdated.setStartDate(LocalDate.of(2022, 8, 1));
        eventToBeUpdated.setEndDate(LocalDate.of(2022, 8, 2));
        eventToBeUpdated.setStartTime(LocalTime.of(22, 0));
        eventToBeUpdated.setEndTime(LocalTime.of(8, 0));
        eventToBeUpdated.setState(EventState.UNAVAILABLE);
        eventToBeUpdated.setComment("some comment");
        eventToBeUpdated.setApplicationStatus(ApplicationStatus.APPROVED);

        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setStartDate(LocalDate.of(2024, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2024, 1, 2));
        testEvent2.setStartTime(LocalTime.of(22, 0));
        testEvent2.setEndTime(LocalTime.of(8, 0));
        testEvent2.setComment("some new comment");

        Mockito.when(sleepEventRepository.findByEventId(Mockito.anyInt())).thenReturn(eventToBeUpdated);

        // then
        assertThrows(ResponseStatusException.class, () -> sleepEventService.updateSleepEvent(eventToBeUpdated.getProviderId(), eventToBeUpdated.getPlaceId(), testEvent2));
    }

    @Test
    public void updateSleep_notProvider() {
        // given
        User testUser2 = new User();
        testUser2.setUserId(5);

        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setStartDate(LocalDate.of(2024, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2024, 1, 2));
        testEvent2.setStartTime(LocalTime.of(22, 0));
        testEvent2.setEndTime(LocalTime.of(8, 0));
        testEvent2.setComment("some new comment");

        // then
        assertThrows(ResponseStatusException.class, () -> sleepEventService.updateSleepEvent(testUser2.getUserId(), testEvent.getPlaceId(), testEvent2));
    }

    @Test
    public void updateSleep_tooShort() {
        // given
        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setStartDate(LocalDate.of(2024, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2024, 1, 1));
        testEvent2.setStartTime(LocalTime.of(22, 0));
        testEvent2.setEndTime(LocalTime.of(22, 30));
        testEvent2.setComment("some new comment");

        // then
        assertThrows(ResponseStatusException.class, () -> sleepEventService.updateSleepEvent(testEvent.getProviderId(), testEvent.getPlaceId(), testEvent2));
    }
    @Test
    public void updateSleep_tooLong() {
        // given
        SleepEvent testEvent2 = new SleepEvent();
        testEvent2.setStartDate(LocalDate.of(2024, 1, 1));
        testEvent2.setEndDate(LocalDate.of(2024, 1, 2));
        testEvent2.setStartTime(LocalTime.of(22, 0));
        testEvent2.setEndTime(LocalTime.of(11, 0));
        testEvent2.setComment("some new comment");

        // then
        assertThrows(ResponseStatusException.class, () -> sleepEventService.updateSleepEvent(testEvent.getProviderId(), testEvent.getPlaceId(), testEvent2));
    }



    @Test
    public void addApplicant_success() {
        User testUser2 = new User();
        testUser2.setUserId(5);

        User testUser3 = new User();
        testUser3.setUserId(6);

        Mockito.when(userRepository.findByUserId(Mockito.anyInt())).thenReturn(testUser3);

        List<Integer> listApplicants = new ArrayList<>();
        listApplicants.add(testUser2.getUserId());

        testUser3.setMyCalendarAsApplicant(events);

        testEvent.setApplicants(listApplicants);

        // when
        SleepEvent updatedEvent = sleepEventService.addApplicant(testUser3.getUserId(), testEvent.getEventId());

        // then
        assertEquals(testEvent.getApplicants().get(0), updatedEvent.getApplicants().get(0));
        assertEquals(testEvent.getApplicants().get(1), updatedEvent.getApplicants().get(1));
    }

    @Test
    public void addApplicant_eventApproved() {
        User testUser2 = new User();
        testUser2.setUserId(5);

        User testUser3 = new User();
        testUser3.setUserId(6);

        Mockito.when(userRepository.findByUserId(Mockito.anyInt())).thenReturn(testUser3);

        List<Integer> listApplicants = new ArrayList<>();
        listApplicants.add(testUser2.getUserId());

        testUser3.setMyCalendarAsApplicant(events);

        testEvent.setApplicants(listApplicants);

        SleepEvent approvedEvent = new SleepEvent();
        approvedEvent.setEventId(20);
        approvedEvent.setProviderId(1);
        approvedEvent.setPlaceId(2);
        approvedEvent.setApplicants(Collections.emptyList());
        approvedEvent.setConfirmedApplicant(5);
        approvedEvent.setStartDate(LocalDate.of(2022, 8, 1));
        approvedEvent.setEndDate(LocalDate.of(2022, 8, 2));
        approvedEvent.setStartTime(LocalTime.of(22, 0));
        approvedEvent.setEndTime(LocalTime.of(8, 0));
        approvedEvent.setState(EventState.UNAVAILABLE);
        approvedEvent.setComment("some comment");
        approvedEvent.setApplicationStatus(ApplicationStatus.APPROVED);

        Mockito.when(sleepEventRepository.findByEventId(Mockito.anyInt())).thenReturn(approvedEvent);

        assertThrows(ResponseStatusException.class, () -> sleepEventService.addApplicant(testUser3.getUserId(), approvedEvent.getEventId()));

    }


    @Test
    public void confirmSleepEvent_success() {
        // when
        User testUser2 = new User();
        testUser2.setUserId(5);

        User testUser3 = new User();
        testUser3.setUserId(6);

        Mockito.when(userRepository.findByUserId(Mockito.anyInt())).thenReturn(testUser3);

        List<Integer> listApplicants = new ArrayList<>();
        listApplicants.add(testUser2.getUserId());
        listApplicants.add(testUser3.getUserId());

        testUser3.setMyCalendarAsApplicant(events);

        testEvent.setApplicants(listApplicants);

        SleepEvent updatedEvent = sleepEventService.confirmSleepEvent(testUser3.getUserId(), testEvent.getEventId());

        // then
        assertEquals(testEvent.getConfirmedApplicant(), updatedEvent.getConfirmedApplicant());
    }


    @Test
    public void confirmSleepEvent_applicantHasNotApplied() {
        // when
        User testUser2 = new User();
        testUser2.setUserId(5);

        User testUser3 = new User();
        testUser3.setUserId(6);

        Mockito.when(userRepository.findByUserId(Mockito.anyInt())).thenReturn(testUser3);

        List<Integer> listApplicants = new ArrayList<>();
        // only user2 is added to the list of applicants
        listApplicants.add(testUser2.getUserId());

        testUser3.setMyCalendarAsApplicant(events);

        testEvent.setApplicants(listApplicants);

        // then
        assertThrows(ResponseStatusException.class, () -> sleepEventService.confirmSleepEvent(testUser3.getUserId(), testEvent.getEventId()));
    }


    @Test
    public void checkIfExpiredOrOver_setToExpired() {
        User testUser2 = new User();
        testUser2.setUserId(5);

        Place anotherTestPlace = new Place();
        anotherTestPlace.setPlaceId(3);
        anotherTestPlace.setProviderId(1);

        List<Place> places = new ArrayList<>();
        places.add(testPlace);
        places.add(anotherTestPlace);

        SleepEvent expiredEvent = new SleepEvent();
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

        // update events
        events.add(expiredEvent);
        // update the sleep events list of testPlace
        testPlace.setSleepEvents(events);

        Mockito.when(placeRepository.findAll()).thenReturn(places);
        // when
        sleepEventService.checkIfExpiredOrOver();

        // then
        assertEquals(EventState.EXPIRED, expiredEvent.getState());
    }
}
