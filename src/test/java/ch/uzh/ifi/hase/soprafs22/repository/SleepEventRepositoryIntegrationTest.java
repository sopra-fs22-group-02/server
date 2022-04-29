package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class SleepEventRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SleepEventRepository sleepEventRepository;

    @Test
    public void findByEventId_success() {
        // given
        SleepEvent event = new SleepEvent();
        event.setProviderId(2);
        event.setPlaceId(3);
        event.setApplicants(null);
        event.setConfirmedApplicant(0);
        event.setStartDate(LocalDate.now());
        event.setStartTime(LocalTime.now());
        event.setEndDate(LocalDate.now());
        event.setEndTime(LocalTime.now().plusHours(12L));
        event.setState(EventState.AVAILABLE);
        event.setComment("some comment");

        entityManager.persist(event);
        entityManager.flush();

        // when
        SleepEvent found = sleepEventRepository.findByEventId(event.getEventId());

        // then
        assertEquals(found.getEventId(), event.getEventId());
        assertEquals(found.getProviderId(), event.getProviderId());
        assertEquals(found.getPlaceId(), event.getPlaceId());
        assertEquals(found.getApplicants(), event.getApplicants());
        assertEquals(found.getConfirmedApplicant(), event.getConfirmedApplicant());
        assertEquals(found.getStartDate(), event.getStartDate());
        assertEquals(found.getStartTime(), event.getStartTime());
        assertEquals(found.getEndDate(), event.getEndDate());
        assertEquals(found.getEndTime(), event.getEndTime());
        assertEquals(found.getState(), event.getState());
        assertEquals(found.getComment(), event.getComment());
    }
}
