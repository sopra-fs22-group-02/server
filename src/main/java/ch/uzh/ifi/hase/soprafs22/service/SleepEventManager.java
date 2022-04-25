package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.repository.PlaceRepository;
import ch.uzh.ifi.hase.soprafs22.repository.SleepEventRepository;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
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
import java.util.List;

@Service
@Transactional
public class SleepEventManager {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final PlaceRepository placeRepository;
    private final SleepEventRepository sleepEventRepository;

    @Autowired
    public SleepEventManager(@Qualifier("placeRepository") PlaceRepository placeRepository, SleepEventRepository sleepEventRepository) {
        this.placeRepository = placeRepository;
        this.sleepEventRepository = sleepEventRepository;
    }

    public SleepEvent createSleepEvent(int providerId, int placeId, SleepEvent newSleepEvent) {

        // find place to which the new sleep event belongs
        Place correspondingPlace = placeRepository.findByPlaceId(placeId);

        // make sure the sleep event doesn't overlap with other sleep events
        List<SleepEvent> listSleepEvents = correspondingPlace.getSleepEvents();
        LocalDateTime startThisEvent = LocalDateTime.of(newSleepEvent.getStartDate(), newSleepEvent.getStartTime());
        LocalDateTime endThisEvent = LocalDateTime.of(newSleepEvent.getEndDate(), newSleepEvent.getEndTime());

        for (SleepEvent event : listSleepEvents){
            LocalDateTime startOtherEvent = LocalDateTime.of(event.getStartDate(), event.getStartTime());
            LocalDateTime endOtherEvent = LocalDateTime.of(event.getEndDate(), event.getEndTime());

            if(startThisEvent.isBefore(endOtherEvent) || endThisEvent.isAfter(startOtherEvent)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "The sleep event cannot overlap with your other sleep events!");
            }
        }

        // make sure the sleep event <= 12 hours
        long timeDifference = startThisEvent.until(endThisEvent, ChronoUnit.HOURS);

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
}
