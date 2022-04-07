package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.entity.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SleepEventGetDTO {
    private int eventId;
    private List<User> applicants;
    private User confirmedApplicant;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private EventState state;
    private String comment;


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
}
