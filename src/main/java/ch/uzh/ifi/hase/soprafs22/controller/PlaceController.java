package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Place Controller
 * This class is responsible for handling all REST request that are related to
 * the place.
 * The controller will receive the request and delegate the execution to the
 * PlaceService and finally return the result.
 */
public class PlaceController {
    @PostMapping("/places/{userId}/{placeId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void createSleepEvent(@PathVariable int userId, @PathVariable int placeId){

    }

    @PostMapping("/places/{userId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void createPlace(@PathVariable int userId){

    }

    @GetMapping("/places/{userId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void getAllPlacesForUser(@PathVariable int userId){

    }

    @GetMapping("/places/{userId}/{placeId}/events")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void getAllSleepEventsForPlace(@PathVariable int userId, @PathVariable int placeId){

    }

    @GetMapping("/places/{userId}/{placeId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void getSleepEvent (@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){

    }

    @DeleteMapping("/places/{userId}/{placeId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deletePlace(@PathVariable int userId, @PathVariable int placeId){

    }

    @DeleteMapping("/places/{userId}/{placeId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deleteSleepEvent(@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){

    }

    @PutMapping("/places/{userId}/{placeId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void updatePlace(@PathVariable int userId, @PathVariable int placeId){

    }

    @PutMapping("/places/{userId}/{placeId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void updateSleepEvent(@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){

    }

}
