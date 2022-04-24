package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.rest.dto.PlaceGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.PlacePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SleepEventGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SleepEventPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapperPlace;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapperSleepEvent;
import ch.uzh.ifi.hase.soprafs22.service.PlaceManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlaceController {

    private final PlaceManager placeManager;

    PlaceController(PlaceManager placeManager){
        this.placeManager = placeManager;
    }

    private final SleepEventManager sleepEventManager;

    PlaceController(SleepEventManager sleepEventManager){
        this.sleepEventManager = sleepEventManager;
    }

/** POST endpoints */

    @PostMapping("/places/{userId}/{placeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SleepEventGetDTO createSleepEvent(@RequestBody SleepEventPostDTO sleepEventPostDTO, @PathVariable int userId, @PathVariable int placeId){
        // convert API SleepEvent to internal representation
        SleepEvent sleepEventInput = DTOMapperSleepEvent.INSTANCE.convertSleepEventPostDTOtoEntity(sleepEventPostDTO);

        // create sleep event
        SleepEvent createdSleepEvent = sleepEventManager.createSleepEvent(sleepEventInput);

        // convert internal representation of sleep event back to API
        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(createdSleepEvent);
    }

    @PostMapping("/places")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaceGetDTO createPlace(@RequestBody PlacePostDTO placePostDTO){
        // convert API place to internal representation
        Place placeInput = DTOMapperPlace.INSTANCE.convertPlacePostDTOtoEntity(placePostDTO);

        // create place
        Place createdPlace = placeManager.createPlace(placeInput);

        // convert internal representation of place back to API
        return DTOMapperPlace.INSTANCE.convertEntityToPlaceGetDTO(createdPlace);
    }

/** GET endpoints */

    @GetMapping("/places")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlaceGetDTO> getPlaces() {
        // fetch all places in the internal representation
        List<Place> places = placeManager.getPlaces();
        List<PlaceGetDTO> placeGetDTOs = new ArrayList<>();

        // convert each place to the API representation
        for (Place place : places) {
            placeGetDTOs.add(DTOMapperPlace.INSTANCE.convertEntityToPlaceGetDTO(place));
        }
        return placeGetDTOs;
    }


    @GetMapping("/places/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<PlaceGetDTO> getAllPlacesForUser(@PathVariable int userId){
        // fetch places in the internal representation
        List<Place> places = placeManager.getAllPlacesForUser(userId);
        List<PlaceGetDTO> placeGetDTOs = new ArrayList<>();

        //convert each place to the API representation
        for (Place place : places) {
            placeGetDTOs.add(DTOMapperPlace.INSTANCE.convertEntityToPlaceGetDTO(place));
        }
        return placeGetDTOs;
    }

    @GetMapping("/places/{userId}/{placeId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<SleepEventGetDTO> getAllSleepEventsForPlace(@PathVariable int userId, @PathVariable int placeId){
        // fetch sleep events in the internal representation
        List<SleepEvent> sleepEvents = sleepEventManager.getAllSleepEventsForPlace(userId, placeId);
        List<SleepEventGetDTO> sleepEventGetDTOs = new ArrayList<>();

        // convert each sleep event to the API representation
        for (SleepEvent sleepEvent : sleepEvents) {
            sleepEventGetDTOs.add(DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(sleepEvent));
        }
        return sleepEventGetDTOs;
    }

    @GetMapping("/places/{userId}/{placeId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public SleepEventGetDTO getSleepEvent (@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){
        // fetch sleep event by id in the internal representation
        SleepEvent sleepEvent = sleepEventManager.findSleepEventById(eventId);

        // convert user to the API representation
        SleepEventGetDTO foundSleepEvent = DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(sleepEvent);
        return foundSleepEvent;
    }

/** DELETE endpoints */

    @DeleteMapping("/places/{placeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlace(@PathVariable int placeId){
        placeManager.deletePlace(placeId);
    }

    @DeleteMapping("/places/{userId}/{placeId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSleepEvent(@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){
        SleepEventManager.deleteSleepEvent(userId, placeId, eventId);
    }

/** PUT endpoints */

    @PutMapping("/places/{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public PlaceGetDTO updatePlace(@RequestBody Place placeUpdates, @PathVariable int placeId){
        // update place
        Place updatedPlace = placeManager.updatePlace(placeUpdates, placeId);

        // convert internal representation of place back to API
        return DTOMapperPlace.INSTANCE.convertEntityToPlaceGetDTO(updatedPlace);
    }

    @PutMapping("/places/{userId}/{placeId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public SleepEventGetDTO updateSleepEvent(@RequestBody SleepEvent eventUpdates, @PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){
        // update SleepEvent
        SleepEvent updatedSleepEvent = SleepEventManager.updateSleepEvent(eventUpdates, userId, placeId, eventId);

        // convert internal representation of event back to API
        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(updatedSleepEvent);
    }
}
