package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.rest.dto.PlaceGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.PlacePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapperPlace;
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

/** POST endpoints */

    @PostMapping("/places/{userId}/{placeId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void createSleepEvent(@PathVariable int userId, @PathVariable int placeId){

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
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void getAllSleepEventsForPlace(@PathVariable int userId, @PathVariable int placeId){

    }

    @GetMapping("/places/{userId}/{placeId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void getSleepEvent (@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){

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
