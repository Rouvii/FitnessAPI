package dat.dao;

import dat.dto.ExerciseDTO;
import dat.entities.Exercise;
import dat.entities.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class ExcersieDAO implements IDao<ExerciseDTO> {
    private static ExcersieDAO instance;
    private static EntityManagerFactory emf;

    private ExcersieDAO(EntityManagerFactory emf) {
        ExcersieDAO.emf = emf;
    }

    public static ExcersieDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new ExcersieDAO(_emf);
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
                return null; // Or throw exception if necessary
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

            // Validate the Session before proceeding with creation
            Session session = em.find(Session.class, exerciseDTO.getSession().getId());
            if (session == null) {
                throw new IllegalArgumentException("Session with ID " + exerciseDTO.getSession().getId() + " not found");
            }

            Exercise exercise = new Exercise();
            exercise.setName(exerciseDTO.getName());
            exercise.setSession(session);
            exercise.setDescription(exerciseDTO.getDescription());
            exercise.setMuscleGroup(exerciseDTO.getMuscleGroup());

            em.persist(exercise);
            em.getTransaction().commit();

            return new ExerciseDTO(exercise); // Return DTO for newly created exercise
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

            // Find the exercise by its ID
            Exercise exercise = em.find(Exercise.class, id); // Use 'id' to find the exercise
            if (exercise == null) {
                throw new IllegalArgumentException("Exercise with ID " + id + " not found");
            }

            // Validate the Session before updating
            Session session = em.find(Session.class, update.getSession().getId());
            if (session == null) {
                throw new IllegalArgumentException("Session with ID " + update.getSession().getId() + " not found");
            }

            // Apply updates to the exercise
            exercise.setName(update.getName());
            exercise.setSession(session);
            exercise.setDescription(update.getDescription());
            exercise.setMuscleGroup(update.getMuscleGroup());

            em.getTransaction().commit(); // Commit the transaction if all goes well
        } catch (Exception e) {
            // Ensure rollback in case of an error
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Rollback the transaction in case of failure
            }
            throw new IllegalStateException("Error updating exercise: " + e.getMessage(), e);
        } finally {
            // Close the EntityManager if it's open
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }


    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the exercise by ID and validate its existence
            Exercise exercise = em.find(Exercise.class, id);
            if (exercise == null) {
                throw new IllegalArgumentException("Exercise with ID " + id + " not found");
            }

            // Remove the exercise from the database
            em.remove(exercise);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new IllegalStateException("Error deleting exercise: " + e.getMessage(), e);
        }
    }
}
