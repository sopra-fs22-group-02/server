package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.ApplicationStatus;
import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.entity.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SleepEventGetDTO {
    private int eventId;
    private int providerId;
    private int placeId;
    private List<Integer> applicants;
    private int confirmedApplicant;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private EventState state;
    private String comment;
    private ApplicationStatus applicationStatus;


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {this.eventId = eventId;}

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {this.providerId = providerId;}

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {this.placeId = placeId;}

    public List<Integer> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<Integer> applicants) {
        this.applicants = applicants;
    }

    public int getConfirmedApplicant() {
        return confirmedApplicant;
    }

    public void setConfirmedApplicant(int confirmedApplicant) {
        this.confirmedApplicant = confirmedApplicant;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
}
