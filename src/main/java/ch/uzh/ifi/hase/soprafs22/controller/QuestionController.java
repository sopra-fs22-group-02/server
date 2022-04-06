package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Question Controller
 * This class is responsible for handling all REST request that are related to
 * Q&A sessions.
 * The controller will receive the request and delegate the execution to the
 * Q&AService and finally return the result.
 */

public class QuestionController {
    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void createQuestion(@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){

    }

    @GetMapping("/questions/{questionId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void getQuestion(@PathVariable int userId, @PathVariable int placeId, @PathVariable int eventId){

    }
}
