package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.PlaceRepository;
import ch.uzh.ifi.hase.soprafs22.repository.SleepEventRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class SleepEventManager {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final PlaceRepository placeRepository;
    private final SleepEventRepository sleepEventRepository;
    private final UserRepository userRepository;

    @Autowired
    public SleepEventManager(@Qualifier("placeRepository") PlaceRepository placeRepository, SleepEventRepository sleepEventRepository, UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.sleepEventRepository = sleepEventRepository;
        this.userRepository = userRepository;
    }

    public SleepEvent createSleepEvent(int providerId, int placeId, SleepEvent newSleepEvent) {

        // find place to which the new sleep event belongs
        Place correspondingPlace = placeRepository.findByPlaceId(placeId);

        // make sure the sleep event doesn't overlap with other sleep events

        // fetch all the events within this place
        List<SleepEvent> listSleepEvents = correspondingPlace.getSleepEvents();

        LocalDateTime startNewEvent = LocalDateTime.of(newSleepEvent.getStartDate(), newSleepEvent.getStartTime());
        LocalDateTime endNewEvent = LocalDateTime.of(newSleepEvent.getEndDate(), newSleepEvent.getEndTime());

        // go through all the events and check whether there are overlaps
        for (SleepEvent event : listSleepEvents){

            LocalDateTime startExistingEvent = LocalDateTime.of(event.getStartDate(), event.getStartTime());
            LocalDateTime endExistingEvent = LocalDateTime.of(event.getEndDate(), event.getEndTime());

            if((startNewEvent.isBefore(startExistingEvent) && endNewEvent.isAfter(startExistingEvent)) ||
                    (startNewEvent.isBefore(endExistingEvent) && endNewEvent.isAfter(endExistingEvent)) ||
                    ((startNewEvent == startExistingEvent) && (endNewEvent == endExistingEvent)) ||
                    ((startNewEvent == startExistingEvent) && (endNewEvent.isAfter(endExistingEvent))) ||
                    ((startNewEvent.isBefore(startExistingEvent)) && (endNewEvent == endExistingEvent)))
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "The new sleep event overlaps with another of your events!");
            }
            /*if((startNewEvent == startExistingEvent) && (endNewEvent == endExistingEvent)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "The new sleep event overlaps with another of your events!");
            }*/
        }

        // make sure the sleep event <= 12 hours
        long timeDifference = startNewEvent.until(endNewEvent, ChronoUnit.HOURS);

        if(timeDifference > 12L){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The sleep event is too long (max 12 hours)!");
        }

        newSleepEvent.setProviderId(providerId);
        newSleepEvent.setPlaceId(placeId);
        // set event state
        newSleepEvent.setState(EventState.AVAILABLE);

        newSleepEvent = sleepEventRepository.save(newSleepEvent);
        sleepEventRepository.flush();

        // add sleep event to the places list of sleep events --> sleep event is saved in database
        correspondingPlace.addSleepEvents(newSleepEvent);

        return newSleepEvent;
    }

    public SleepEvent confirmSleepEvent(int userId, int eventId) {
        // find user by Id
        User userById = userRepository.findByUserId(userId);

        // find SleepEvent by Id
        SleepEvent confirmSleepEvent = sleepEventRepository.findByEventId(eventId);

        // check if the applicant that^s about to be accepted actually applied for this sleep event
        List<User> applicants = confirmSleepEvent.getApplicants();
        Boolean confirmedApplicantIsInList = Boolean.FALSE;
        for (User applicant : applicants) {
            if (applicant.getUserId() == userId) {
                confirmedApplicantIsInList = Boolean.TRUE;
                break;
            }
        }

        if (confirmedApplicantIsInList == Boolean.FALSE) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The applicant you want to accept has not applied for this sleep event!");
        }

        if(userById == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This user who you wanted to accept does not exist!");
        }

        // set confirmed applicant
        confirmSleepEvent.setConfirmedApplicant(userById);
        // change event state to unavailable for other users
        confirmSleepEvent.setState(EventState.UNAVAILABLE);
        // reset applicant list in sleep event
        confirmSleepEvent.setApplicants(Collections.emptyList());

        return confirmSleepEvent;
    }

    public List<SleepEvent> getAllSleepEventsForPlace(int placeId){
        Place place = placeRepository.findByPlaceId(placeId);

        if(place == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This place does not exist!");
        }

        return place.getSleepEvents();
    }

    public SleepEvent findSleepEventById(int eventId){
        SleepEvent sleepEvent =sleepEventRepository.findByEventId(eventId);
        if(sleepEvent == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This sleep event does not exist!");
        }
        return sleepEvent;
    }

    public void deleteSleepEvent(int eventId){

        //check if there's a confirmed applicant!!

        SleepEvent eventToBeDeleted = sleepEventRepository.findByEventId(eventId);

        if(eventToBeDeleted == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This sleep does not exist and can therefore not be deleted!");
        }

        Place place = placeRepository.findByPlaceId(eventToBeDeleted.getPlaceId());
        List<SleepEvent> listSleepEvents = place.getSleepEvents();

        listSleepEvents.removeIf(event -> event.getEventId() == eventId);
        sleepEventRepository.delete(sleepEventRepository.findByEventId(eventId));
    }

    public SleepEvent updateSleepEvent(int userId, int eventId, SleepEvent updates){

        //check if there's a confirmed applicant!!

        SleepEvent eventToBeUpdated = sleepEventRepository.findByEventId(eventId);

        if(eventToBeUpdated == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This sleep event does not exist!");
        }

        // only the provider is allowed to modify an event
        if(userId != eventToBeUpdated.getProviderId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You are not the provider of this sleep event and therefore cannot edit it!");
        }

        eventToBeUpdated.setStartDate(updates.getStartDate());
        eventToBeUpdated.setEndDate(updates.getEndDate());
        eventToBeUpdated.setStartTime(updates.getStartTime());
        eventToBeUpdated.setEndTime(updates.getEndTime());
        eventToBeUpdated.setComment(updates.getComment());

        return eventToBeUpdated;
    }

    public SleepEvent addApplicant(int userId, int eventId){
        SleepEvent eventToBeUpdated = sleepEventRepository.findByEventId(eventId);
        User applicant = userRepository.findByUserId(userId);

        eventToBeUpdated.addApplicant(applicant);
        return eventToBeUpdated;
    }
}
