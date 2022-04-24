package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Location;
import ch.uzh.ifi.hase.soprafs22.entity.User;

public class PlacePostDTO {

    private int providerId;
    //private Location location;
    private String description;

    public int getProviderId(){
        return providerId;
    }

    public void setProviderId(int providerId){
        this.providerId = providerId;
    }

    /*public Location getLocation(){
        return location;
    }

    public void setLocation(Location location){
        this.location = location;
    }*/

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
