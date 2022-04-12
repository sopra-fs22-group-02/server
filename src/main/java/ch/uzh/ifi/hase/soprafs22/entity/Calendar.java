package ch.uzh.ifi.hase.soprafs22.entity;

import java.util.List;

public class Calendar {
    private List<SleepEvent> SleepEventListAsApplicant;
    private List<SleepEvent> SleepEventListAsProvider;

    public void addEventsAsProvider(SleepEvent event){
        SleepEventListAsProvider.add(event);
    }

    public void addEventAsApplicant(SleepEvent event){
        SleepEventListAsApplicant.add(event);
    }

    public void deleteEventAsProvider(SleepEvent event){
        SleepEventListAsProvider.remove(event);
    }
}
