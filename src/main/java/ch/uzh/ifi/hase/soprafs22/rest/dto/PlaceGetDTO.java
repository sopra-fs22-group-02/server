package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Location;
import ch.uzh.ifi.hase.soprafs22.entity.User;

public class PlaceGetDTO {

    private int placeId;
    private int providerId;
    private Location location;
    private String description;
    private String pictureOfThePlace;
    //private List<SleepEvent> sleepEvents;

    public int getPlaceId(){
        return placeId;
    }

    public void setPlaceId(int placeId){
        this.placeId = placeId;
    }

    public int getProviderId(){
        return providerId;
    }

    public void setProviderId(int providerId){
        this.providerId = providerId;
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getPictureOfThePlace(){
        return pictureOfThePlace;
    }

    public void setPictureOfThePlace(String pictureOfThePlace){
        this.pictureOfThePlace = pictureOfThePlace;
    }

    /*public List<SleepEvent> getSleepEvents(){
        return sleepEvents;
    }

    public void setSleepEvents(List<SleepEvent> sleepEvents){
        this.sleepEvents = sleepEvents;
    }*/
}
