package dat.dao;

import dat.dto.ExerciseDTO;
import dat.entities.Exercise;
import dat.entities.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class ExerciseDAO implements IDao<ExerciseDTO> {
    private static ExerciseDAO instance;
    private static EntityManagerFactory emf;

    public ExerciseDAO(EntityManagerFactory emf) {
        ExerciseDAO.emf = emf;
    }

    public static ExerciseDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new ExerciseDAO(_emf);
        }
        return instance;
    }

    @Override
    public List<ExerciseDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Exercise> exercises = em.createQuery("SELECT e FROM Exercise e", Exercise.class).getResultList();
            return exercises.stream().map(ExerciseDTO::new).toList();
        } catch (PersistenceException e) {
            throw new IllegalStateException("Error fetching exercises: " + e.getMessage(), e);
        }
    }

    @Override
    public ExerciseDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Exercise exercise = em.find(Exercise.class, id);
            if (exercise == null) {
                throw new IllegalArgumentException("Exercise with ID " + id + " not found");
            }
            return new ExerciseDTO(exercise);
        } catch (PersistenceException e) {
            throw new IllegalStateException("Error fetching exercise by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public ExerciseDTO create(ExerciseDTO exerciseDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            List<Session> sessions = null;
            if (exerciseDTO.getSessionIds() != null) {
                sessions = exerciseDTO.getSessionIds().stream()
                        .map(sessionId -> em.find(Session.class, sessionId))
                        .toList();
                if (sessions.contains(null)) {
                    throw new IllegalArgumentException("One or more sessions not found");
                }
            }

            Exercise exercise = new Exercise(exerciseDTO, sessions);
            em.persist(exercise);
            em.getTransaction().commit();

            return new ExerciseDTO(exercise);
        } catch (Exception e) {
            throw new IllegalStateException("Error creating exercise: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(int id, ExerciseDTO update) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Exercise exercise = em.find(Exercise.class, id);
            if (exercise == null) {
                throw new IllegalArgumentException("Exercise with ID " + id + " not found");
            }

            List<Session> sessions = null;
            if (update.getSessionIds() != null) {
                EntityManager finalEm = em;
                sessions = update.getSessionIds().stream()
                        .map(sessionId -> finalEm.find(Session.class, sessionId))//Skal måske fikses
                        .toList();
                if (sessions.contains(null)) {
                    throw new IllegalArgumentException("One or more sessions not found");
                }
            }

            exercise.setName(update.getName());
            exercise.setSessions(sessions);
            exercise.setDescription(update.getDescription());
            exercise.setMuscleGroup(update.getMuscleGroup());

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new IllegalStateException("Error updating exercise: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Exercise exercise = em.find(Exercise.class, id);
            if (exercise == null) {
                throw new IllegalArgumentException("Exercise with ID " + id + " not found");
            }

            em.remove(exercise);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new IllegalStateException("Error deleting exercise: " + e.getMessage(), e);
        }
    }
}