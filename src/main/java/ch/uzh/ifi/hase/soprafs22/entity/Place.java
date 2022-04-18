package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Place")
public class Place implements Serializable {

    private static final int serialVersionUID = 1;

    @Id
    @GeneratedValue
    private int placeId;

    @OneToOne
    private User provider;

    @OneToOne
    private Location location;

    @Column
    private String description;

    @Column
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
