package app.repositories;

import app.models.Opinion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class TemporaryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Class clazz, Object obj) {
        save(clazz, obj, null);
    }

    public void save(Class clazz, Object obj, Long id) {
        if (id == null || entityManager.find(clazz, id) == null) {
            entityManager.persist(obj);
        } else {
            entityManager.merge(obj);
        }
        entityManager.flush();
    }

    public void detach(Object obj) {
        entityManager.detach(obj);
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
