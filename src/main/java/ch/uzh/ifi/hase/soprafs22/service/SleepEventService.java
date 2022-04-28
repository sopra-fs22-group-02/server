package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.PlaceRepository;
import ch.uzh.ifi.hase.soprafs22.repository.SleepEventRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class SleepEventService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final PlaceRepository placeRepository;
    private final SleepEventRepository sleepEventRepository;
    private final UserRepository userRepository;

    @Autowired
    public SleepEventService(@Qualifier("placeRepository") PlaceRepository placeRepository, SleepEventRepository sleepEventRepository, UserRepository userRepository) {
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

            if(((startNewEvent.isBefore(startExistingEvent)) && (endNewEvent.isAfter(startExistingEvent))) ||
                    ((startNewEvent.isBefore(endExistingEvent)) && (endNewEvent.isAfter(endExistingEvent))) ||
                    ((startNewEvent.equals(startExistingEvent)) && (endNewEvent.equals(endExistingEvent))) ||
                    ((startNewEvent.equals(startExistingEvent)) && (endNewEvent.isAfter(endExistingEvent))) ||
                    ((startNewEvent.isBefore(startExistingEvent)) && (endNewEvent.equals(endExistingEvent))) ||
                    ((startNewEvent.isBefore(startExistingEvent)) && (endNewEvent.isAfter(endExistingEvent))) ||
                    ((startNewEvent.isAfter(startExistingEvent)) && (endNewEvent.isBefore(endExistingEvent))))
            {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "The new sleep event overlaps with another of your events!");
            }
        }

        // make sure the sleep event <= 12 hours
        long timeDifference = startNewEvent.until(endNewEvent, ChronoUnit.MINUTES);

        if(timeDifference > 720L){
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

    public SleepEvent updateSleepEvent(int userId, int eventId, SleepEvent updates){

        SleepEvent eventToBeUpdated = sleepEventRepository.findByEventId(eventId);

        if(eventToBeUpdated == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This sleep event does not exist!");
        }

        // as soon as an applicant has been accepted,
        // the provider cannot update the event anymore
        if(eventToBeUpdated.getConfirmedApplicant() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Since an applicant has been accepted for this sleep event, it cannot be updated anymore!");
        }

        // only the provider is allowed to modify an event
        if(userId != eventToBeUpdated.getProviderId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You are not the provider of this sleep event and therefore cannot edit it!");
        }

        // make sure new time slot <= 12h
        LocalDateTime startUpdated = LocalDateTime.of(updates.getStartDate(), updates.getStartTime());
        LocalDateTime endUpdated = LocalDateTime.of(updates.getEndDate(), updates.getEndTime());

        long timeDifference = startUpdated.until(endUpdated, ChronoUnit.MINUTES);

        if(timeDifference > 720L){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The sleep event is too long (max 12 hours) and can therefore not be updated!");
        }

        eventToBeUpdated.setStartDate(updates.getStartDate());
        eventToBeUpdated.setEndDate(updates.getEndDate());
        eventToBeUpdated.setStartTime(updates.getStartTime());
        eventToBeUpdated.setEndTime(updates.getEndTime());
        eventToBeUpdated.setComment(updates.getComment());

        return eventToBeUpdated;
    }

    public void deleteSleepEvent(int eventId, int userId){

        SleepEvent eventToBeDeleted = sleepEventRepository.findByEventId(eventId);

        if(eventToBeDeleted == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This sleep does not exist and can therefore not be deleted!");
        }

        // as soon as an applicant has been accepted, the provider cannot delete the event anymore
        if(eventToBeDeleted.getConfirmedApplicant() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Since an applicant has been accepted for this sleep event, it cannot be deleted!");
        }

        // only the provider is allowed to modify an event
        if(userId != eventToBeDeleted.getProviderId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You are not the provider of this sleep event and therefore cannot delete it!");
        }

        Place place = placeRepository.findByPlaceId(eventToBeDeleted.getPlaceId());
        List<SleepEvent> listSleepEvents = place.getSleepEvents();

        listSleepEvents.removeIf(event -> event.getEventId() == eventId);
        sleepEventRepository.delete(sleepEventRepository.findByEventId(eventId));
    }


    /** apply for sleep event */

    public SleepEvent addApplicant(int userId, int eventId) {
        SleepEvent eventToBeUpdated = sleepEventRepository.findByEventId(eventId);
        User applicant = userRepository.findByUserId(userId);

        eventToBeUpdated.addApplicant(applicant);
        return eventToBeUpdated;
    }


    /** accept applicant*/

    public SleepEvent confirmSleepEvent(int userId, int eventId) {
        // find user by Id
        User userById = userRepository.findByUserId(userId);

        // find SleepEvent by Id
        SleepEvent confirmSleepEvent = sleepEventRepository.findByEventId(eventId);

        // check if the applicant that's about to be accepted actually applied for this sleep event
        List<User> applicants = confirmSleepEvent.getApplicants();
        boolean confirmedApplicantIsInList = Boolean.FALSE;
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


    /** methods regarding state*/

    @Scheduled(fixedDelay = 5000)
    public void checkIfExpiredOrOver(){
        List<Place> allPlaces = placeRepository.findAll();
        // stop if there are no places at all
        if(allPlaces.isEmpty()){return;}

        List<SleepEvent> toBeDeleted = new ArrayList<>();

        // go through all places
        for(Place place : allPlaces){
            List<SleepEvent> allEventsOfPlace = place.getSleepEvents();

            // go to next place if there are no events in this place
            if(allEventsOfPlace.isEmpty()){break;}

            // check all events of this place
            for(SleepEvent event : allEventsOfPlace){
                LocalDateTime startThisEvent = LocalDateTime.of(event.getStartDate(), event.getStartTime());
                LocalDateTime endThisEvent = LocalDateTime.of(event.getEndDate(), event.getEndTime());

                // set event to "expired" when it has started AND no one has applied
                if(LocalDateTime.now().isAfter(startThisEvent) && event.getConfirmedApplicant() == null){
                    event.setState(EventState.EXPIRED);
                }
                // delete event when it is over
                if(LocalDateTime.now().isAfter(endThisEvent)){
                    toBeDeleted.add(event);
                }
            }
        }

        // go through the events that are over and delete them
        for(SleepEvent event : toBeDeleted){
            deleteExpiredSleepEvent(event.getEventId());
        }
    }

    // helper function for checkIfExpiredOrOver()
    // cannot use deleteSleepEvent(), because an expired sleep event does have a confirmed applicant
    // (would stop at first if), but it has to be deleted anyway, because it is over
    private void deleteExpiredSleepEvent(int eventId){
        SleepEvent expiredEvent = sleepEventRepository.findByEventId(eventId);

        Place place = placeRepository.findByPlaceId(expiredEvent.getPlaceId());
        List<SleepEvent> listSleepEvents = place.getSleepEvents();

        listSleepEvents.removeIf(event -> event.getEventId() == eventId);
        sleepEventRepository.delete(sleepEventRepository.findByEventId(eventId));
    }

    public void setStateToAvailable(int eventId){
        SleepEvent eventToBeUpdated = sleepEventRepository.findByEventId(eventId);

        if(eventToBeUpdated == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This sleep event does not exist!");
        }

        eventToBeUpdated.setState(EventState.AVAILABLE);
    }
}
