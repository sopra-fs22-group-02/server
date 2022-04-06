package ch.uzh.ifi.hase.soprafs22.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public class PlaceController {
/** POST endpoints */

    @PostMapping("/places/{userId}/{placeId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void createSleepEvent(@PathVariable int userId, @PathVariable int placeId){

    }

    @PostMapping("/places/{userId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void createPlace(@PathVariable int userId){

    }

/** GET endpoints */

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

/** DELETE endpoints */

    @DeleteMapping("/places/{userId}/{placeId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deletePlace(@PathVariable int userId, @PathVariable int placeId){

    }

    @DeleteMapping("/places/{userId}/{placeId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void deleteSleepEvent(@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){

    }

/** PUT endpoints */

    @PutMapping("/places/{userId}/{placeId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void updatePlace(@PathVariable int userId, @PathVariable int placeId){

    }

    @PutMapping("/places/{userId}/{placeId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void updateSleepEvent(@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){

    }
}
