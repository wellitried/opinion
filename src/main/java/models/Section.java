package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonManagedReference
    @OneToMany(mappedBy = "section", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Question> questions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "opinionPollId", nullable = false)
    @JsonBackReference
    private OpinionPoll opinionPoll;

    private String text;

    private Integer timeToResponse;

    private Integer number;


    public Section() {
    }


    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public OpinionPoll getOpinionPoll() {
        return opinionPoll;
    }

    public void setOpinionPoll(OpinionPoll opinionPoll) {
        this.opinionPoll = opinionPoll;
    }

    public Integer getTimeToResponse() {
        return timeToResponse;
    }

    public void setTimeToResponse(Integer timeToResponse) {
        this.timeToResponse = timeToResponse;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer index) {
        this.number = index;
    }
}