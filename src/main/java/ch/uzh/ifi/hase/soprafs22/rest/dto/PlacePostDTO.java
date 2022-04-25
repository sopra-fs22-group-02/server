package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.entity.Location;
import ch.uzh.ifi.hase.soprafs22.entity.User;

public class PlacePostDTO {

    private int providerId;
    private String name;
    private String address;
    private Campus closestCampus;
    private String description;

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
}
