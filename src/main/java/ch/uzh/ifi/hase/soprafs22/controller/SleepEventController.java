package ch.uzh.ifi.hase.soprafs22.controller;


import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SleepEventGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SleepEventPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapperSleepEvent;
import ch.uzh.ifi.hase.soprafs22.service.SleepEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SleepEventController {

    private final SleepEventService sleepEventService;

    @Autowired
    SleepEventController(SleepEventService sleepEventService){
        this.sleepEventService = sleepEventService;
    }

    /** POST endpoints */


    @PostMapping("/places/{userId}/{placeId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SleepEventGetDTO createSleepEvent(@PathVariable int userId, @PathVariable int placeId, @RequestBody SleepEventPostDTO sleepEventPostDTO){
        SleepEvent newSleepEvent = DTOMapperSleepEvent.INSTANCE.convertSleepEventPostDTOtoEntity(sleepEventPostDTO);

        SleepEvent createdSleepEvent = sleepEventService.createSleepEvent(userId, placeId, newSleepEvent);

        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(createdSleepEvent);
    }

    // accept an applicant
    @PostMapping("/places/{userId}/events/{eventId}/accept")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SleepEventGetDTO confirmedApplicant(@PathVariable int userId, @PathVariable int eventId) {
        SleepEvent confirmSleepEvent = sleepEventService.confirmSleepEvent(userId, eventId);

        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(confirmSleepEvent);
    }

    // add an applicant to the sleep event's applicant list (when user applies for sleep event)
    @PostMapping("/places/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SleepEventGetDTO addApplicantToList(@PathVariable int userId, @PathVariable int eventId){

        SleepEvent updatedSleepEvent = sleepEventService.addApplicant(userId, eventId);

        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(updatedSleepEvent);
    }

/** GET endpoints */

    // fetch all the events within one place
    @GetMapping("/places/{placeId}/events")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<SleepEventGetDTO> getAllSleepEventsForPlace(@PathVariable int placeId){
        // fetch all sleep events for this place in the internal representation
        List<SleepEvent> sleepEvents = sleepEventService.getAllSleepEventsForPlace(placeId);
        List<SleepEventGetDTO> sleepEventGetDTOs = new ArrayList<>();

        // convert each sleep event to the API representation
        for (SleepEvent sleepEvent : sleepEvents) {
            sleepEventGetDTOs.add(DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(sleepEvent));
        }
        return sleepEventGetDTOs;
    }

    // fetch all available sleep events for a place
    @GetMapping("/places/{placeId}/events/available")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<SleepEventGetDTO> getAllAvailableSleepEventsForPlace(@PathVariable int placeId){
        // fetch all sleep events for this place in the internal representation
        List<SleepEvent> sleepEvents = sleepEventService.getAllAvailableSleepEventsForPlace(placeId);
        List<SleepEventGetDTO> sleepEventGetDTOs = new ArrayList<>();

        // convert each sleep event to the API representation
        for (SleepEvent sleepEvent : sleepEvents) {
            sleepEventGetDTOs.add(DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(sleepEvent));
        }
        return sleepEventGetDTOs;
    }

    // fetch all available sleep events
    @GetMapping("/places/events/available")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<SleepEventGetDTO> getAllAvailableSleepEvents(){
        // fetch all sleep events in the internal representation
        List<SleepEvent> sleepEvents = sleepEventService.getAllAvailableSleepEvents();
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
        SleepEvent sleepEvent = sleepEventService.findSleepEventById(eventId);

        // convert user to the API representation
        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(sleepEvent);
    }

/** DELETE endpoints */

    @DeleteMapping("/places/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSleepEvent(@PathVariable int eventId, @PathVariable int userId){ sleepEventService.deleteSleepEvent(eventId, userId); }

/** PUT endpoints */

    // update event
    @PutMapping("/places/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public SleepEventGetDTO updateSleepEvent(@PathVariable int userId, @PathVariable int eventId, @RequestBody SleepEvent sleepEventUpdates){
        SleepEvent updatedSleepEvent = sleepEventService.updateSleepEvent(userId, eventId, sleepEventUpdates);

        return DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(updatedSleepEvent);
    }
}
