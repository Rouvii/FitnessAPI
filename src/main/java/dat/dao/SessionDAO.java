package dat.dao;

import dat.dto.ExerciseDTO;
import dat.dto.SessionDTO;
import dat.entities.Session;
import dat.entities.Exercise;
import dat.exception.ApiException;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.util.List;

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

    public void saveOrUpdateUser(User user) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User existingUser = em.find(User.class, user.getUsername());
            if (existingUser != null) {
                // Merge eksisterende bruger
                em.merge(existingUser);
            } else {
                // Persist en ny bruger
                em.persist(user);
            }
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new IllegalStateException("Error persisting/updating user: " + e.getMessage(), e);
        }
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
    public SessionDTO create(SessionDTO sessionDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create session based on sessionDTO
            Session session = new Session(sessionDTO);

            // Persist the user if it is not already persistent
            if (sessionDTO.getUser() != null && sessionDTO.getUser().getUsername() != null) {
                // Try to fetch the user from the database using the username
                User user = em.find(User.class, sessionDTO.getUser().getUsername());

                // If the user is not persistent, create and persist it
                if (user == null) {
                    user = new User(sessionDTO.getUser().getUsername(), sessionDTO.getUser().getPassword());
                    em.persist(user); // Persist the user
                }

                // Attach the persisted user to the session
                session.setUser(user);
            }

            // Add exercises to the session if they exist
            if (sessionDTO.getExercises() != null) {
                for (ExerciseDTO exerciseDTO : sessionDTO.getExercises()) {
                    Exercise exercise = em.find(Exercise.class, exerciseDTO.getId());
                    if (exercise == null) {
                        throw new IllegalArgumentException("Exercise with ID " + exerciseDTO.getId() + " not found");
                    }
                    session.addExercise(exercise); // Add exercise to the session
                }
            }

            // Use merge instead of persist to handle detached entities
            session = em.merge(session);

            // Commit the transaction
            em.getTransaction().commit();

            // Return the created session as DTO
            return new SessionDTO(session);

        } catch (PersistenceException e) {
            // If an error occurs during persistence, throw an IllegalStateException
            throw new IllegalStateException("Error creating session: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(int id, SessionDTO sessionDTO) {

    }



    @Override
    public void updateReal(int id, Session session) {
        System.out.println("__________________________");
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Hent den eksisterende session
            Session existingSession = em.find(Session.class, id);
            if (existingSession == null) {
                throw new IllegalArgumentException("Session not found");
            }

            // Gennemgå de øvelser, der skal tilføjes
            for (Exercise exercise : session.getExercises()) {
                // Hent eksisterende øvelse fra databasen
                Exercise existingExercise = em.find(Exercise.class, exercise.getId());
                if (existingExercise == null) {
                    throw new IllegalArgumentException("Exercise not found");
                }

                // Sørg for, at sessionen ikke allerede er tilføjet til øvelsens session-liste
                if (!existingExercise.getSessions().contains(existingSession)) {
                    // Tilføj sessionen til øvelsens session-liste
                    existingExercise.getSessions().add(existingSession);
                }

                // Tilføj øvelsen til sessionens exercise-liste
                existingSession.addExercise(existingExercise);
            }

            // Gem ændringerne i begge enheder
            em.getTransaction().commit();
            System.out.println("__________________________");
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
            session.getExercises().forEach(exercise -> {
                exercise.setSessions(null);
                em.merge(exercise);
            });
            em.remove(session);
            em.getTransaction().commit();
        }
    }

    public void addExercisesToSession(int sessionId, List<Integer> exerciseIds) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the session by ID
            Session session = em.find(Session.class, sessionId);
            if (session == null) {
                throw new ApiException(404, "Session not found");
            }

            // Initialize the exercise collection
            session.getExercises().size();


            // Find and add each exercise to the session
            for (int exerciseId : exerciseIds) {
                Exercise exercise = em.find(Exercise.class, exerciseId);
                if (exercise == null) {
                    throw new ApiException(404, "Exercise with ID " + exerciseId + " not found");
                }
                session.addExercise(exercise);
            }

            // Merge the session to update it in the database
            em.merge(session);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new IllegalStateException("Error adding exercises to session: " + e.getMessage(), e);
        }
    }

    public User findUserByUsername(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(User.class, username);
        }
    }

    public Session getSessionEntityById(int sessionId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Session.class, sessionId);
        }
    }
}