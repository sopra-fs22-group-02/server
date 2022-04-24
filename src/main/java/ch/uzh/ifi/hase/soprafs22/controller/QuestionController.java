package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Question;
import ch.uzh.ifi.hase.soprafs22.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.QuestionPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapperQuestion;
import ch.uzh.ifi.hase.soprafs22.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuestionController {

    private final QuestionService questionService;

    QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

/** POST endpoints */

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public QuestionGetDTO createQuestion(@RequestBody QuestionPostDTO questionPostDTO) {
        // convert API question to internal representation
        Question questionInput = DTOMapperQuestion.INSTANCE.convertQuestionPostDTOtoEntity(questionPostDTO);

        // create question
        Question createdQuestion = questionService.createQuestion(questionInput);

        // convert internal representation of question back to API
        return DTOMapperQuestion.INSTANCE.convertEntityToQuestionGetDTO(createdQuestion);
    }

/** GET endpoints */

    @GetMapping("/questions/{questionId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public QuestionGetDTO getQuestion(@PathVariable int questionId){
        // fetch question by id in the internal representation
        Question question = questionService.findQuestionById(questionId);

        // convert question to the API representation
        QuestionGetDTO foundQuestion = DTOMapperQuestion.INSTANCE.convertEntityToQuestionGetDTO(question);
        return foundQuestion;
    }
}
