package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.constant.ApplicationStatus;
import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SleepEventGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SleepEventPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperSleepEventTest {
  @Test
  public void testCreateSleepEvent_fromSleepEventPostDTO_toSleepEvent_success() {
    // create SleepEventPostDTO
    SleepEventPostDTO sleepEventPostDTO = new SleepEventPostDTO();
    sleepEventPostDTO.setStartDate(LocalDate.now());
    sleepEventPostDTO.setEndDate(LocalDate.of(2022,5,1));
    sleepEventPostDTO.setStartTime(LocalTime.now());
    sleepEventPostDTO.setEndTime(LocalTime.of(20,0));
    sleepEventPostDTO.setComment("this is a comment.");

    // MAP -> Create sleep event
    SleepEvent sleepEvent = DTOMapperSleepEvent.INSTANCE.convertSleepEventPostDTOtoEntity(sleepEventPostDTO);

    // check content
    assertEquals(sleepEventPostDTO.getStartDate(), sleepEvent.getStartDate());
    assertEquals(sleepEventPostDTO.getEndDate(), sleepEvent.getEndDate());
    assertEquals(sleepEventPostDTO.getStartTime(), sleepEvent.getStartTime());
    assertEquals(sleepEventPostDTO.getEndTime(), sleepEvent.getEndTime());
    assertEquals(sleepEventPostDTO.getComment(), sleepEvent.getComment());
  }

  @Test
  public void testGetSleepEvent_fromSleepEvent_toSleepEventGetDTO_success() {
    // create SleepEvent
    SleepEvent sleepEvent = new SleepEvent();
    sleepEvent.setStartDate(LocalDate.now());
    sleepEvent.setEndDate(LocalDate.of(2022,5,1));
    sleepEvent.setStartTime(LocalTime.now());
    sleepEvent.setEndTime(LocalTime.of(20,0));
    sleepEvent.setState(EventState.AVAILABLE);
    sleepEvent.setComment("this is a comment.");
    sleepEvent.setApplicationStatus(null);

    // MAP -> Create SleepEventGetDTO
    SleepEventGetDTO sleepEventGetDTO = DTOMapperSleepEvent.INSTANCE.convertEntityToSleepEventGetDTO(sleepEvent);

    // check content
    assertEquals(sleepEvent.getEventId(), sleepEventGetDTO.getEventId());
    assertEquals(sleepEvent.getProviderId(), sleepEventGetDTO.getProviderId());
    assertEquals(sleepEvent.getPlaceId(), sleepEventGetDTO.getPlaceId());
    assertEquals(sleepEvent.getStartDate(), sleepEventGetDTO.getStartDate());
    assertEquals(sleepEvent.getEndDate(), sleepEventGetDTO.getEndDate());
    assertEquals(sleepEvent.getStartTime(), sleepEventGetDTO.getStartTime());
    assertEquals(sleepEvent.getEndTime(), sleepEventGetDTO.getEndTime());
    assertEquals(sleepEvent.getState(), sleepEventGetDTO.getState());
    assertEquals(sleepEvent.getComment(), sleepEventGetDTO.getComment());
  }
}
