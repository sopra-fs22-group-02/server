package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.time.LocalDateTime;

public class NotificationPostDTO {

    private String message;


    /** getters and setters */
    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
