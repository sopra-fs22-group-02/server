package ch.uzh.ifi.hase.soprafs22.entity;
import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.constant.Gender;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class SleepEvent {
    private static final int serialVersionUID = 1;

    @Id
    @GeneratedValue
    private int eventId;
    private List<User> applicants;
    private User confirmedApplicant;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private EventState state;
    private String comment;


/** getters and setters */

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {this.eventId = eventId;}

    public List<User> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<User> applicants) {
        this.applicants = applicants;
    }

    public User getConfirmedApplicant() {
        return confirmedApplicant;
    }

    public void setConfirmedApplicant(User confirmedApplicant) {
        this.confirmedApplicant = confirmedApplicant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public EventState getState() {
        return state;
    }

    public void setState(EventState state) {
        this.state = state;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public void addApplicant(User applicantToBeAdded){
        applicants.add(applicantToBeAdded);
    }

    public void removeApplicant(User applicantToBeRemoved){
        applicants.remove(applicantToBeRemoved);
    }
}
