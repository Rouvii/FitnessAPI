package dat.dao;

import dat.dto.SessionDTO;
import dat.entities.Session;
import dat.entities.Exercise;
import dat.exception.ApiException;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;

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
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Session session = new Session(sessionDto);
            User user = em.find(User.class, sessionDto.getUser().getUsername());
            if (user == null) {
                throw new EntityNotFoundException("User not found with ID: " + sessionDto.getUser().getUsername());
            }
            session.setUser(user);
            List<Exercise> exercises = sessionDto.getExercises().stream()
                    .map(exerciseDto -> {
                        Exercise exercise = em.find(Exercise.class, exerciseDto.getId());
                        if (exercise == null) {
                            throw new EntityNotFoundException("Exercise not found with ID: " + exerciseDto.getId());
                        }
                        return exercise;
                    })
                    .collect(Collectors.toList());
            session.setExercise(exercises);
            em.persist(session);
            em.getTransaction().commit();
            return new SessionDTO(session);
        } catch (EntityNotFoundException e) {
            em.getTransaction().rollback();
            throw new ApiException(404, e.getMessage());
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new ApiException(500, "An error occurred while creating the session: " + e.getMessage());
        } finally {
            em.close();
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
                    .map(exerciseId -> em.find(Exercise.class, exerciseId))
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
                exercise.setSessions(null);
                em.merge(exercise);
            });
            em.remove(session);
            em.getTransaction().commit();
        }
    }
}