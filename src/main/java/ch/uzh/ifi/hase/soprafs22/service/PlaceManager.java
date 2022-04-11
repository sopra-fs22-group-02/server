package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.PlaceRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class PlaceManager {

  private final Logger log = LoggerFactory.getLogger(PlaceManager.class);

  private final PlaceRepository placeRepository;

  private final UserRepository userRepository;

  @Autowired
  public PlaceManager(@Qualifier("placeRepository") PlaceRepository placeRepository, UserRepository userRepository) {
    this.placeRepository = placeRepository;
      this.userRepository = userRepository;
  }

  public List<Place> getPlaces(){
      return this.placeRepository.findAll();
  }

  public List<Place> getAllPlacesForUser(int userId){
      User userById = userRepository.findByUserId(userId);

      List<Place> allPlaces = getPlaces();
      List<Place> usersPlaces = new ArrayList<>();

      for (Place place : allPlaces) {
          if (place.getProvider().getUserId() == userId) {
              usersPlaces.add(place);
          }
      }

      if (userById == null) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  "The user provided is not found!");
      }
      return usersPlaces;
  }

  public Place createPlace(Place newPlace) {

      checkIfPlaceExists(newPlace);

      newPlace = placeRepository.save(newPlace);
      placeRepository.flush();

      log.debug("Created Information for User: {}", newPlace);
      return newPlace;
  }

  private void checkIfPlaceExists(Place placeToBeCreated) {
      Place placeByProvider = placeRepository.findByProvider(placeToBeCreated.getProvider());

      if (placeByProvider != null) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  String.format("You have already created a place. Therefore, the place could not be created!"));
      }
  }

  public void deletePlace(int userId, int placeId) {
      List<Place> usersPlaces = placeRepository.findByUserId(userId);
      for (Place place : usersPlaces) {
          placeRepository.delete(placeRepository.findByPlaceId(placeId));
      }
  }

}
