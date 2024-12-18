package dat.dao;

import dat.dao.IDao;
import dat.entities.Exercise;
import dat.entities.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class SessionDAO implements IDao<Session> {

    private final EntityManagerFactory emf;

    public SessionDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Session> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Session s", Session.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Session getById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Session.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public Session create(Session session) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(session);
            transaction.commit();
            return session;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(int id, Session session) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Session existing = em.find(Session.class, id);
            if (existing != null) {
                existing.setName(session.getName());
                existing.setExercises(session.getExercises()); // Update exercise list
                em.merge(existing);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Session session = em.find(Session.class, id);
            if (session != null) {
                em.remove(session);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Add exercises to a session
    public void addExercisesToSession(int sessionId, List<Exercise> exercises) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Session session = em.find(Session.class, sessionId);
            if (session != null) {
                session.getExercises().addAll(exercises); // Add exercises to the session
                em.merge(session); // Merge the updated session
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
