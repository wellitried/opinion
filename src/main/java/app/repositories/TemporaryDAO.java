package app.repositories;

import app.models.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class TemporaryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Object save(Class clazz, Object obj) {
        return save(clazz, obj, null);
    }

    public Object save(Class clazz, Object obj, Long id) {
        if (id == null || entityManager.find(clazz, id) == null) {
            entityManager.persist(obj);
        } else {
            entityManager.merge(obj);
        }
        entityManager.flush();
        return entityManager.find(clazz, ((Model) obj).getId());
    }

    public void detach(Object obj) {
        entityManager.detach(obj);
    }

    public void removeSection(Long id) {
        Section section = entityManager.find(Section.class, id);
        if (section != null) {
            List questionsIds = entityManager
                    .createQuery("select q.id from Question q where q.section.id=:id")
                    .setParameter("id", id)
                    .getResultList();
            questionsIds.forEach(questionId -> removeQuestion((Long) questionId));
            remove(Section.class, id);
        }
    }

    public void removeQuestion(Long id) {
        Question question = entityManager.find(Question.class, id);
        if (question != null) {
            List answersIds = entityManager
                    .createQuery("select a.id from Answer a where a.question.id=:id")
                    .setParameter("id", id)
                    .getResultList();
            answersIds.forEach(answerId -> removeAnswer((Long) answerId));
            remove(Question.class, id);
        }
    }

    public void removeAnswer(Long id) {
        remove(Answer.class, id);
    }

    public void remove(Class clazz, Long id) {
        Object obj = entityManager.find(clazz, id);
        if (obj != null) {
            entityManager.remove(obj);
        }
    }

    public Opinion findOpinionByCode(String code) {
        try {
            return (Opinion) entityManager
                    .createQuery("select o from Opinion o where o.code=:code")
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Opinion findOpinionByPublicCode(String publicCode) {
        try {
            return (Opinion) entityManager
                    .createQuery("select o from Opinion o where o.publicCode=:publicCode")
                    .setParameter("publicCode", publicCode)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
