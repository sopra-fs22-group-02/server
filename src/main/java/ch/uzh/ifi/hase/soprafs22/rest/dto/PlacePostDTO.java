package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.Location;
import ch.uzh.ifi.hase.soprafs22.entity.User;

public class PlacePostDTO {

    private User provider;
    private Location location;

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

}
