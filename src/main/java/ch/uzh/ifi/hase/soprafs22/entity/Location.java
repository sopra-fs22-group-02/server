package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;

public class Location {
    private String postCode;
    //private Coordinate coordinates;
    private Campus closestCampus;
    private int distanceToClosestCampus;
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
