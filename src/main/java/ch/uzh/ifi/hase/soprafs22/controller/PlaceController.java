package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapperSleepEvent;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapperPlace;
import ch.uzh.ifi.hase.soprafs22.service.PlaceManager;
import ch.uzh.ifi.hase.soprafs22.service.SleepEventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlaceController {

    private final PlaceManager placeManager;
    private final SleepEventManager sleepEventManager;

    @Autowired
    PlaceController(PlaceManager placeManager, SleepEventManager sleepEventManager){
        this.placeManager = placeManager;
        this.sleepEventManager = sleepEventManager;
    }

    /** POST endpoints */

    @PostMapping("/places/{placeId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SleepEventGetDTO createSleepEvent(@PathVariable int placeId, @RequestBody SleepEventPostDTO sleepEventPostDTO){
        SleepEvent newSleepEvent = DTOMapperSleepEvent.INSTANCE.convertSleepEventPostDTOtoEntity(sleepEventPostDTO);

        SleepEvent createdSleepEvent = sleepEventManager.createSleepEvent(placeId, newSleepEvent);

        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(createdSleepEvent);
    }


    @PostMapping("/places")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
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

    @GetMapping("/places/{placeId}/events")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<SleepEventGetDTO> getAllSleepEventsForPlace(@PathVariable int placeId){
        // fetch all sleep events for this place in the internal representation
        List<SleepEvent> sleepEvents = sleepEventManager.getAllSleepEventsForPlace(placeId);
        List<SleepEventGetDTO> sleepEventGetDTOs = new ArrayList<>();

        // convert each sleep event to the API representation
        for (SleepEvent sleepEvent : sleepEvents) {
            sleepEventGetDTOs.add(DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(sleepEvent));
        }
        return sleepEventGetDTOs;
    }

    @GetMapping("/places/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SleepEventGetDTO getSleepEvent (@PathVariable int eventId) {
        // fetch sleep event by id in the internal representation
        SleepEvent sleepEvent = sleepEventManager.findSleepEventById(eventId);

        // convert user to the API representation
        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(sleepEvent);
    }

/** DELETE endpoints */

    @DeleteMapping("/places/{placeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlace(@PathVariable int placeId){
        placeManager.deletePlace(placeId);
    }

    @DeleteMapping("/places/{userId}/{placeId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deleteSleepEvent(@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){

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
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void updateSleepEvent(@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){

    }
}
