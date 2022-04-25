package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
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
          if (place.getProviderId() == userId) {
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
      Place placeByProvider = placeRepository.findByProviderId(placeToBeCreated.getProviderId());

      if (placeByProvider != null) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  String.format("You have already created a place. Therefore, the place could not be created!"));
      }
  }

  public void deletePlace(int placeId) {
          placeRepository.delete(placeRepository.findByPlaceId(placeId));

  }

  public Place updatePlace(Place placeUpdated, int placeId) {
      // check that if user wants to update location to NULL (meaning he leaves the field empty)
      /*if(placeUpdated.getLocation() == null){
          throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                  "Location cannot be NULL!");
      }*/
      // find place by ID
      Place UpdatePlace = placeRepository.findByPlaceId(placeId);

      // throw exception if place doesn't exist
      if(UpdatePlace == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                  "The place was not found!");
      }

      String UpdatedName = placeUpdated.getName();
      String UpdatedAddress = placeUpdated.getAddress();
      Campus UpdatedClosestCampus = placeUpdated.getClosestCampus();
      String UpdatedDescription = placeUpdated.getDescription();
      String UpdatedPictureOfThePlace = placeUpdated.getPictureOfThePlace();

      UpdatePlace.setName(UpdatedName);
      UpdatePlace.setAddress(UpdatedAddress);
      UpdatePlace.setClosestCampus(UpdatedClosestCampus);
      UpdatePlace.setDescription(UpdatedDescription);
      UpdatePlace.setPictureOfThePlace(UpdatedPictureOfThePlace);

      return UpdatePlace;

  }
}
