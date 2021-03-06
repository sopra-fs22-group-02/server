package ch.uzh.ifi.hase.soprafs22.entity;
import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.constant.ApplicationStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Entity
@Table(name = "SLEEPEVENT")
public class SleepEvent implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue
    private int eventId;

    @Column(nullable = false)
    private int providerId;

    @Column (nullable = false)
    private int placeId;

    @ElementCollection
    private List<Integer> applicants;

    @Column
    private int confirmedApplicant;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private EventState state;

    @Column
    private String comment;

    @Column
    private ApplicationStatus applicationStatus;


/** getters and setters */

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

    public Integer getConfirmedApplicant() {
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

    public List<Integer> addApplicant(int applicantToBeAdded){
        applicants.add(applicantToBeAdded);
        return applicants;
    }

    public void removeApplicant(int applicantToBeRemoved){
        applicants.remove(applicantToBeRemoved);
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
}
