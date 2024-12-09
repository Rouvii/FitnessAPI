package dat.dao;/* @auther: Frederik Dupont */

import dat.dto.ExerciseDTO;
import dat.entities.Exercise;
import dat.entities.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

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

    public List<ExerciseDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<ExerciseDTO> exercises = em.createQuery("SELECT e FROM Exercise e", ExerciseDTO.class).getResultList();
            return exercises;
        }
    }

    public ExerciseDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            ExerciseDTO exercise = em.find(ExerciseDTO.class, id);
            return exercise;
        }

    }

    @Override
    public ExerciseDTO create(ExerciseDTO exerciseDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Session session = em.find(Session.class, exerciseDTO.getSession().getId());
            Exercise exercise = new Exercise();
            exercise.setName(exerciseDTO.getName());
            exercise.setSession(session);
            exercise.setDescription(exerciseDTO.getDescription());
            exercise.setMuscleGroup(exerciseDTO.getMuscleGroup());
            em.persist(exercise);
            em.getTransaction().commit();
            return exerciseDTO;
        }
    }

    @Override
    public void update(ExerciseDTO exerciseDTO, ExerciseDTO update) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Exercise exercise = em.find(Exercise.class, exerciseDTO.getId());
            exercise.setName(update.getName());
            exercise.setSession(new Session(update.getSession()));
            exercise.setDescription(update.getDescription());
            exercise.setMuscleGroup(update.getMuscleGroup());
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Exercise exercise = em.find(Exercise.class, id);
            em.remove(exercise);
            em.getTransaction().commit();
        }
    }
}
