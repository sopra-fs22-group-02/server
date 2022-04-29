package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SleepEventPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SleepEventGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapperSleepEvent {

  DTOMapperSleepEvent INSTANCE = Mappers.getMapper(DTOMapperSleepEvent.class);

  @Mapping(source = "startDate", target = "startDate")
  @Mapping(source = "endDate", target = "endDate")
  @Mapping(source = "startTime", target = "startTime")
  @Mapping(source = "endTime", target = "endTime")
  @Mapping(source = "comment", target = "comment")
  SleepEvent convertSleepEventPostDTOtoEntity(SleepEventPostDTO placePostDTO);

  @Mapping(source = "eventId", target = "eventId")
  @Mapping(source = "providerId", target = "providerId")
  @Mapping(source = "placeId", target = "placeId")
  @Mapping(source = "applicants", target = "applicants")
  @Mapping(source = "confirmedApplicant", target = "confirmedApplicant")
  @Mapping(source = "startDate", target = "startDate")
  @Mapping(source = "endDate", target = "endDate")
  @Mapping(source = "startTime", target = "startTime")
  @Mapping(source = "endTime", target = "endTime")
  @Mapping(source = "state", target = "state")
  @Mapping(source = "comment", target = "comment")
  @Mapping(source = "applicationStatus", target = "applicationStatus")
  SleepEventGetDTO convertEntityToSleepEventGetDTO(SleepEvent sleepEvent);
}
