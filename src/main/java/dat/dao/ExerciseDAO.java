package dat.dao;

import dat.dao.IDao;
import dat.entities.Exercise;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ExerciseDAO implements IDao<Exercise> {

    private final EntityManagerFactory emf;

    public ExerciseDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Exercise> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Exercise> query = em.createQuery("SELECT e FROM Exercise e", Exercise.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Exercise getById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Exercise.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public Exercise create(Exercise exercise) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(exercise);
            em.getTransaction().commit();
            return exercise;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(int id, Exercise updatedExercise) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Exercise existingExercise = em.find(Exercise.class, id);
            if (existingExercise != null) {
                existingExercise.setName(updatedExercise.getName());
                existingExercise.setMuscleGroup(updatedExercise.getMuscleGroup());
                existingExercise.setDescription(updatedExercise.getDescription());
                existingExercise.setSets(updatedExercise.getSets());
                existingExercise.setSessions(updatedExercise.getSessions());
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Exercise exercise = em.find(Exercise.class, id);
            if (exercise != null) {
                em.remove(exercise);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
