package dat.dao;

import dat.entities.Session;
import dat.exception.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SessionDAO implements IDao<Session> {

    private static SessionDAO instance;
    private static EntityManagerFactory emf;


    private SessionDAO(EntityManagerFactory emf) {
        SessionDAO.emf = emf;
    }

    // Singleton pattern
    public static SessionDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new SessionDAO(_emf);
        }
        return instance;
    }

    @Override
    public List<Session> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Session> query = em.createQuery("SELECT s FROM Session s", Session.class);
            return query.getResultList();
        }
    }

    @Override
    public Session getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Session session = em.find(Session.class, id);
            if (session == null) {
                throw new ApiException(404, "Session not found");
            }
            return session;
        }
    }

    @Override
    public Session create(Session session) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(session);
            em.getTransaction().commit();
            return session;
        }
    }

    @Override
    public void update(Session session, Session updatedSession) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Session existingSession = em.find(Session.class, session.getId());
            if (existingSession == null) {
                throw new ApiException(404, "Session not found");
            }

            // Update properties
            existingSession.setExercise(updatedSession.getExercise());

            em.merge(existingSession);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Session session = em.find(Session.class, id);
            if (session == null) {
                throw new ApiException(404, "Session not found");
            }

            // Remove associated exercises if cascade is not configured
            session.getExercise().forEach(exercise -> {
                exercise.setSession(null);
                em.merge(exercise);
            });

            em.remove(session);
            em.getTransaction().commit();
        }
    }
}
