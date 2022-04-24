package ch.uzh.ifi.hase.soprafs22.rest.dto;


import ch.uzh.ifi.hase.soprafs22.constant.Answer;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.entity.User;

public class QuestionGetDTO {

    private int questionId;
    private User questionTo;
    private User questionFrom;
    private SleepEvent relatedEvent;
    private String content;
    private Answer answer;
    private Boolean isForApplicant;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public User getUserQuestionTo() {
        return questionTo;
    }

    public void setUserQuestionTo(User questionTo) {
        this.questionTo = questionTo;
    }

    public User getUserQuestionFrom() {
        return questionFrom;
    }

    public void setUserQuestionFrom(User questionFrom) {
        this.questionFrom = questionFrom;
    }

    public SleepEvent getRelatedEvent() {
        return relatedEvent;
    }

    public void setRelatedEvent(SleepEvent relatedEvent) {
        this.relatedEvent = relatedEvent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Boolean getIsForApplicant() {
        return isForApplicant;
    }

    public void setIsForApplicant(Boolean isForApplicant) {
        this.isForApplicant = isForApplicant;
    }
}
