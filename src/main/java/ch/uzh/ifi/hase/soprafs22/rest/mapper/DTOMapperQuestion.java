package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.Question;
import ch.uzh.ifi.hase.soprafs22.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.QuestionPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapperQuestion {

  DTOMapperQuestion INSTANCE = Mappers.getMapper(DTOMapperQuestion.class);

  @Mapping(source = "content", target = "content")
  Question convertQuestionPostDTOtoEntity(QuestionPostDTO questionPostDTO);

  @Mapping(source = "questionId", target = "questionId")
  @Mapping(source = "questionTo", target = "questionTo")
  @Mapping(source = "questionFrom", target = "questionFrom")
  @Mapping(source = "relatedEvent", target = "relatedEvent")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "answer", target = "answer")
  @Mapping(source = "isForApplicant", target = "isForApplicant")
  QuestionGetDTO convertEntityToQuestionGetDTO(Question question);
}
