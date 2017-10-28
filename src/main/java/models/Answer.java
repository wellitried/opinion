package models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonBackReference
    private Question question;

    private String answerText;

    private boolean chosen;

    private Long navigateToSectionId;


    public Answer() {
    }


    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public Long getNavigateToSectionId() {
        return navigateToSectionId;
    }

    public void setNavigateToSectionId(Long navigateToSectionId) {
        this.navigateToSectionId = navigateToSectionId;
    }
}