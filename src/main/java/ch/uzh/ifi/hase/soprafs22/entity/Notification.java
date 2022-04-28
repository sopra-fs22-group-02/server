package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATION")
public class Notification implements Serializable{

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue
    private int notificationId;

    @Column
    private String message;

    @Column
    private int receiverId;

    @Column
    private LocalDateTime creationDate;


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

    public int getReceiverId(){
        return receiverId;
    }

    public void setReceiverId(int receiverId){
        this.receiverId = receiverId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}

