package ch.uzh.ifi.hase.soprafs22.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

public class QuestionController {

/** POST endpoints */

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void createQuestion(){

    }

/** GET endpoints */

    @GetMapping("/questions/{questionId}")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void getQuestion(@PathVariable int questionId){

    }
}
