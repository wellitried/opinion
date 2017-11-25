package app.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "opinion")
public class Opinion implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Opinion template;

    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String publicCode;

    @JsonManagedReference
    @OneToMany(mappedBy = "opinion", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Section> sections;

    private Date creationDate;

    private Date finishDate;

    private String title;


    public Opinion() {
    }


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicCode() {
        return publicCode;
    }

    public void setPublicCode(String publicCode) {
        this.publicCode = publicCode;
    }

    public Opinion getTemplate() {
        return template;
    }

    public void setTemplate(Opinion template) {
        this.template = template;
    }
}
