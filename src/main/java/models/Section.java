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

    private String tempText;


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

    public String getTempText() {
        return tempText;
    }

    public void setTempText(String tempText) {
        this.tempText = tempText;
    }

    public OpinionPoll getOpinionPoll() {
        return opinionPoll;
    }

    public void setOpinionPoll(OpinionPoll opinionPoll) {
        this.opinionPoll = opinionPoll;
    }
}