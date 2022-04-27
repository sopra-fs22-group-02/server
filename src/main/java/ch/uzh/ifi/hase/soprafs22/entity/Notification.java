package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NOTIFICATION")
public class Notification implements Serializable{

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue
    private int notificationId;

    @Column
    private String message;

    @OneToOne
    private User sender;

    @OneToOne
    private User receiver;


    /** getters and setters */

    public int getNotificationId(){
        return notificationId;
    }

    public void setNotificationId(int notificationId){
        this.notificationId = notificationId;
    }

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
