package ch.uzh.ifi.hase.soprafs22.controller;


import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SleepEventGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SleepEventPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapperSleepEvent;
import ch.uzh.ifi.hase.soprafs22.service.SleepEventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SleepEventController {

    private final SleepEventManager sleepEventManager;

    @Autowired
    SleepEventController(SleepEventManager sleepEventManager){
        this.sleepEventManager = sleepEventManager;
    }

    /** POST endpoints */


    @PostMapping("/places/{userId}/{placeId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SleepEventGetDTO createSleepEvent(@PathVariable int userId, @PathVariable int placeId, @RequestBody SleepEventPostDTO sleepEventPostDTO){
        SleepEvent newSleepEvent = DTOMapperSleepEvent.INSTANCE.convertSleepEventPostDTOtoEntity(sleepEventPostDTO);

        SleepEvent createdSleepEvent = sleepEventManager.createSleepEvent(userId, placeId, newSleepEvent);

        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(createdSleepEvent);
    }

    // accept an applicant
    @PostMapping("/places/{userId}/events/{eventId}/accept")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SleepEventGetDTO confirmedApplicant(@PathVariable int userId, @PathVariable int eventId) {
        SleepEvent confirmSleepEvent = sleepEventManager.confirmSleepEvent(userId, eventId);

        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(confirmSleepEvent);
    }

    // add an applicant to the sleep event's applicant list (when user applies for sleep event)
    @PostMapping("/places/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SleepEventGetDTO addApplicantToList(@PathVariable int userId, @PathVariable int eventId){

        SleepEvent updatedSleepEvent = sleepEventManager.addApplicant(userId, eventId);

        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(updatedSleepEvent);
    }

/** GET endpoints */

    // fetch all the events within one place
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

    // fetch one specific event
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

    @DeleteMapping("/places/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSleepEvent(@PathVariable int eventId, @PathVariable int userId){ sleepEventManager.deleteSleepEvent(eventId, userId); }

/** PUT endpoints */

    // if the event has started without anyone applying, it is set to "expired, but the provider can change the start time and set it to "available" again
    @PutMapping("/places/events/{eventId}/AVAILABLE")
    @ResponseStatus(HttpStatus.OK)
    public void setEventToAvailable(@PathVariable int eventId){
        sleepEventManager.setStateToAvailable(eventId);
    }

    // update event
    @PutMapping("/places/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public SleepEventGetDTO updateSleepEvent(@PathVariable int userId, @PathVariable int eventId, @RequestBody SleepEvent sleepEventUpdates){
        SleepEvent updatedSleepEvent = sleepEventManager.updateSleepEvent(userId, eventId, sleepEventUpdates);

        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(updatedSleepEvent);
    }
}
