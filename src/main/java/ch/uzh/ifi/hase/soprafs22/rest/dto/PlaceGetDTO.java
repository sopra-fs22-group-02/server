package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.entity.Location;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;

import java.util.List;

public class PlaceGetDTO {

    private int placeId;
    private int providerId;
    private String name;
    private String address;
    private Campus closestCampus;
    private String description;
    private String pictureOfThePlace;
    private List<SleepEvent> sleepEvents;

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

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public Campus getClosestCampus(){
        return closestCampus;
    }

    public void setClosestCampus(Campus closestCampus){
        this.closestCampus = closestCampus;
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

    public List<SleepEvent> getSleepEvents(){
        return sleepEvents;
    }

    public void setSleepEvents(List<SleepEvent> sleepEvents){
        this.sleepEvents = sleepEvents;
    }
}
