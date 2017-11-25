package app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "answer")
public class Answer implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonBackReference
    private Question question;

    private String answerText;

    private Boolean chosen;

    private Long navigateToSectionId;

    public Answer() {
    }


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long value) {
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

    public Boolean getChosen() {
        return chosen;
    }

    public void setChosen(Boolean chosen) {
        this.chosen = chosen;
    }

    public Long getNavigateToSectionId() {
        return navigateToSectionId;
    }

    public void setNavigateToSectionId(Long navigateToSectionId) {
        this.navigateToSectionId = navigateToSectionId;
    }

}