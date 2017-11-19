package app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonManagedReference
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Answer> answers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sectionId", nullable = false)
    @JsonBackReference
    private Section section;

    private boolean multipleAnswers;

    private boolean customNavigation;

    private String questionText;


    public Question() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long value) {
        this.id = value;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public boolean isMultipleAnswers() {
        return multipleAnswers;
    }

    public void setMultipleAnswers(boolean multipleAnswers) {
        this.multipleAnswers = multipleAnswers;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public boolean isCustomNavigation() {
        return customNavigation;
    }

    public void setCustomNavigation(boolean customNavigation) {
        this.customNavigation = customNavigation;
    }
}