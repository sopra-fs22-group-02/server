package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "PLACE")
public class Place implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue
    private int placeId;

    @Column(nullable = false, unique = true)
    private int providerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column
    private Campus closestCampus;

    @Column
    private String description;

    @Column
    private String pictureOfThePlace;

    @OneToMany(fetch = FetchType.EAGER)
    private List<SleepEvent> sleepEvents;


/** getters and setters */

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

    public String  getAddress(){
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

    public void addSleepEvents(SleepEvent newSleepEvent){
        this.sleepEvents.add(newSleepEvent);
    }

}
