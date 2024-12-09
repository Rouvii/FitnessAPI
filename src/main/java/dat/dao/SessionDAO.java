package dat.dao;

import dat.dto.SessionDTO;
import dat.entities.Session;
import dat.entities.Exercise;
import dat.exception.ApiException;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.util.List;
import java.util.stream.Collectors;

public class SessionDAO implements IDao<SessionDTO> {

    private static SessionDAO instance;
    private static EntityManagerFactory emf;

    public SessionDAO(EntityManagerFactory emf) {
        SessionDAO.emf = emf;
    }

    public static SessionDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new SessionDAO(_emf);
        }
        return instance;
    }

    @Override
    public List<SessionDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Session> sessions = em.createQuery("SELECT s FROM Session s", Session.class).getResultList();
            return sessions.stream().map(SessionDTO::new).toList();
        } catch (PersistenceException e) {
            throw new IllegalStateException("Error fetching sessions: " + e.getMessage(), e);
        }
    }

    @Override
    public SessionDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Session session = em.find(Session.class, id);
            if (session == null) {
                throw new ApiException(404, "Session not found");
            }
            return new SessionDTO(session);
        }
    }

    @Override
    public SessionDTO create(SessionDTO sessionDto) {
        Session session = new Session(sessionDto);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(session);
            em.getTransaction().commit();
            return new SessionDTO(session);
        }
    }

    @Override
    public void update(int id, SessionDTO updatedSession) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Session existingSession = em.find(Session.class, id);
            if (existingSession == null) {
                throw new ApiException(404, "Session not found");
            }

            List<Exercise> exercises = updatedSession.getExercises().stream()
                    .map(dto -> new Exercise(dto, existingSession))
                    .collect(Collectors.toList());
            existingSession.setExercise(exercises);

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

            session.getExercise().forEach(exercise -> {
                exercise.setSession(null);
                em.merge(exercise);
            });

            em.remove(session);
            em.getTransaction().commit();
        }
    }


    public void save(Session session) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(session);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void saveUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}