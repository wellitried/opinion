package app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "section")
public class Section implements Model {

    enum SectionType{
        QUESTIONS,
        RESPONDENT_INFO,
        INFORMATION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonManagedReference
    @OneToMany(mappedBy = "section", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Question> questions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "opinionId", nullable = false)
    @JsonBackReference
    private Opinion opinion;

    private String text;

    private Integer timeToResponse;

    @Enumerated(EnumType.STRING)
    private SectionType type;

    public Section() {
    }


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long value) {
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

    public Opinion getOpinion() {
        return opinion;
    }

    public void setOpinion(Opinion opinion) {
        this.opinion = opinion;
    }

    public Integer getTimeToResponse() {
        return timeToResponse;
    }

    public void setTimeToResponse(Integer timeToResponse) {
        this.timeToResponse = timeToResponse;
    }

    public SectionType getType() {
        return type;
    }

    public void setType(SectionType type) {
        this.type = type;
    }

}