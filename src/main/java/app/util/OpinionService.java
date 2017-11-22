package app.util;

import app.models.Answer;
import app.models.Opinion;
import app.models.Question;
import app.models.Section;
import app.repositories.TemporaryDAO;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

public class OpinionService {

    public static OpinionService getInstance() {
        return OpinionService.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final OpinionService instance = new OpinionService();
    }

    public Opinion makeOpinionTemplate(String code, TemporaryDAO dao) {
        Opinion opinion = new Opinion();
        opinion.setCode(code);
        opinion.setPublicCode(Util.getInstance().generateCode());
        opinion.setCreationDate(new Date());

        dao.save(Opinion.class, opinion, opinion.getId());

        return opinion;
    }

    public Opinion makeOpinionFromTemplate(Opinion original, TemporaryDAO dao) {
        Opinion clone = new Opinion();
        dao.detach(original);
        BeanUtils.copyProperties(original, clone);

        //TODO refactoring
        clone.setCreationDate(new Date());
        clone.setId(null);
        clone.setTemplate(original);
        clone.setCode(null);
        clone.setPublicCode(null);
        List<Section> originalSections = clone.getSections();
        clone.setSections(null);
        dao.save(Opinion.class, clone);
        clone.setSections(originalSections);

        if (clone.getSections() != null) {
            clone.getSections().forEach(section -> {
                dao.detach(section);
                section.setId(null);
                section.setOpinion(clone);
                List<Question> originalQuestions = section.getQuestions();
                section.setQuestions(null);
                dao.save(Section.class, section);
                section.setQuestions(originalQuestions);

                if (section.getQuestions() != null) {
                    section.getQuestions().forEach(question -> {
                        dao.detach(question);
                        question.setId(null);
                        question.setSection(section);
                        List<Answer> originalAnswers = question.getAnswers();
                        dao.save(Question.class, question);
                        question.setAnswers(originalAnswers);

                        if (question.getAnswers() != null) {
                            question.getAnswers().forEach(answer -> {
                                dao.detach(answer);
                                answer.setId(null);
                                answer.setQuestion(question);
                                dao.save(Answer.class, answer);
                            });
                        }
                    });
                }
            });
        }

        return clone;
    }
}
