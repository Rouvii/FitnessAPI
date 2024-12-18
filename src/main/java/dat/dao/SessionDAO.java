package dat.dao;

import dat.dto.SessionDTO;
import dat.entities.Session;
import dat.entities.Exercise;
import dat.exception.ApiException;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

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
            return sessions.stream().map(SessionDTO::new).collect(Collectors.toList());
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
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Fetch or create user
            User user = em.find(User.class, sessionDto.getUser().getUsername());
            if (user == null) {
                user = new User(sessionDto.getUser().getUsername(), sessionDto.getUser().getPassword());
                em.persist(user);
            }

            // Map exercises
            List<Exercise> exercises = sessionDto.getExerciseIds().stream()
                    .map(id -> {
                        Exercise exercise = em.find(Exercise.class, id);
                        if (exercise == null) {
                            throw new ApiException(404, "Exercise with ID " + id + " not found");
                        }
                        return exercise;
                    })
                    .collect(Collectors.toList());

            // Create session
            Session session = new Session();
            session.setName(sessionDto.getName());
            session.setUser(user);
            session.setExercise(exercises);

            // Persist session
            em.persist(session);
            em.getTransaction().commit();
            return new SessionDTO(session);
        } catch (Exception e) {
            throw new ApiException(400, "Error creating session: " + e.getMessage());
        }
    }

    @Override
    public void update(int id, SessionDTO updatedSession) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find existing session
            Session existingSession = em.find(Session.class, id);
            if (existingSession == null) {
                throw new ApiException(404, "Session not found");
            }

            // Update exercises
            List<Exercise> updatedExercises = updatedSession.getExerciseIds().stream()
                    .map(exerciseId -> {
                        Exercise exercise = em.find(Exercise.class, exerciseId);
                        if (exercise == null) {
                            throw new ApiException(404, "Exercise with ID " + exerciseId + " not found");
                        }
                        return exercise;
                    })
                    .collect(Collectors.toList());

            existingSession.setName(updatedSession.getName());
            existingSession.setExercise(updatedExercises);

            em.merge(existingSession);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new ApiException(400, "Error updating session: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find session
            Session session = em.find(Session.class, id);
            if (session == null) {
                throw new ApiException(404, "Session not found");
            }

            // Clear relationships with exercises
            session.getExercise().forEach(exercise -> {
                exercise.getSessions().remove(session); // Update exercise sessions list
                em.merge(exercise);
            });

            em.remove(session); // Remove the session
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new ApiException(400, "Error deleting session: " + e.getMessage());
        }
    }

    // Utility method to fetch user by username
    public User findUserByUsername(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        }
    }

    // Utility method to save a new user
    public void saveUser(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
    }

    public List<Exercise> findExercisesByIds(List<Integer> ids) {
        try (EntityManager em = emf.createEntityManager()) {
            return ids.stream()
                    .map(id -> {
                        Exercise exercise = em.find(Exercise.class, id);
                        if (exercise == null) throw new ApiException(404, "Exercise ID " + id + " not found");
                        return exercise;
                    })
                    .collect(Collectors.toList());
        }
    }
}
