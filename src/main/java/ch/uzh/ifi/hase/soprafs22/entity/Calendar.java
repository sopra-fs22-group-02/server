package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "CALENDAR")
public class Calendar implements Serializable {

    @Id
    @GeneratedValue
    private int calendarId;

    @OneToMany
    private List<SleepEvent> sleepEventListAsApplicant;

    @OneToMany
    private List<SleepEvent> sleepEventListAsProvider;

    public List<SleepEvent> getSleepEventListAsApplicant() {
        return sleepEventListAsApplicant;
    }

    public void setSleepEventListAsApplicant(List<SleepEvent> sleepEventListAsApplicant) {this.sleepEventListAsApplicant = sleepEventListAsApplicant;}

    public List<SleepEvent> getSleepEventListAsProvider() {
        return sleepEventListAsProvider;
    }

    public void setSleepEventListAsProvider(List<SleepEvent> sleepEventListAsProvider) {this.sleepEventListAsProvider = sleepEventListAsProvider;}

    public void addEventsAsProvider(SleepEvent event){
        sleepEventListAsProvider.add(event);
    }

    public void addEventAsApplicant(SleepEvent event){
        sleepEventListAsApplicant.add(event);
    }

    public void deleteEventAsProvider(SleepEvent event){
        sleepEventListAsProvider.remove(event);
    }
}

