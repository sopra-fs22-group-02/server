package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "LOCATION")
public class Location implements Serializable {

    private static final int serialVersionUID = 1;

    @Column
    private String postCode;

    //private Coordinate coordinates;

    @Column
    private Campus closestCampus;

    @Column
    private int distanceToClosestCampus;

    // for now, the address is the primary key of Location.
    // maybe we should add a "LocationID" instead of using this as primary key.
    @Id
    private String address;

/** getters and setters */

    public String getPostCode(){
        return postCode;
    }

    public void setPostCode(String postCode){
        this.postCode = postCode;
    }

    /*public Coordinate getCoordinates(){
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates){
        this.coordinates = coordinates;
    }*/

    public Campus getClosestCampus(){
        return closestCampus;
    }

    public void setClosestCampus(Campus closestCampus){
        this.closestCampus = closestCampus;
    }

    public int getDistanceToClosestCampus(){
        return distanceToClosestCampus;
    }

    public void setDistanceToClosestCampus(int distanceToClosestCampus){
        this.distanceToClosestCampus = distanceToClosestCampus;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }


/** methods */

    /*public int calculateDistance(Coordinate coordinates){
            return int;
    }*/
}
