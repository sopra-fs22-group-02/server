package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.entity.User;

public class NotificationPostDTO {

    private String message;
    private User sender;
    private User receiver;


    /** getters and setters */
    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public User getSender(){
        return sender;
    }

    public void setSender(User sender){
        this.sender = sender;
    }

    public User getReceiver(){
        return receiver;
    }

    public void setReceiver(User receiver){
        this.receiver = receiver;
    }
}
