package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("sleepEventRepository")
public interface SleepEventRepository extends JpaRepository<SleepEvent, Long> {
    SleepEvent findByEventId(int eventId);
}
