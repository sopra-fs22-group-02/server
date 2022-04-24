package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Question;
import ch.uzh.ifi.hase.soprafs22.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class QuestionService {

  private final Logger log = LoggerFactory.getLogger(QuestionService.class);

  private final QuestionRepository questionRepository;

  @Autowired
  public QuestionService(@Qualifier("questionRepository") QuestionRepository questionRepository) {
    this.questionRepository = questionRepository;
  }

  public List<Question> getQuestions() {
    return this.questionRepository.findAll();
  }

  public Question createQuestion(Question newQuestion) {

    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newQuestion = questionRepository.save(newQuestion);
    questionRepository.flush();

    log.debug("Created Information for Question: {}", newQuestion);
    return newQuestion;
  }

  // returns a single question by ID
  public Question findQuestionById(int questionId) {
      Question questionById = questionRepository.findByQuestionId(questionId);

      String baseErrorMessage = "The %s provided %s not found!";
      if (questionById == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                  String.format(baseErrorMessage, "Id", "is"));
        }
      return questionById;
    }

}
