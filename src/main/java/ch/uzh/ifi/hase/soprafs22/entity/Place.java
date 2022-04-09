package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

public class Place {
    private static final int serialVersionUID = 1;

    @Id
    @GeneratedValue
    private int placeId;
    private User provider;
    private Location location;
    private String description;
    private String pictureOfThePlace;
    //private List<SleepEvent> sleepEvents;

/** getters and setters */

    public int getPlaceId(){
        return placeId;
    }

    public void setPlaceId(int placeId){
        this.placeId = placeId;
    }

    public User getProvider(){
        return provider;
    }

    public void setProvider(User provider){
        this.provider = provider;
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
