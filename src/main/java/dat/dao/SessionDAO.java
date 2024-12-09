package dat.dao;

import dat.dto.SessionDTO;
import dat.entities.Session;
import dat.entities.Exercise;
import dat.exception.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

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
            TypedQuery<SessionDTO> query = em.createQuery("SELECT new dat.dto.SessionDTO(s) FROM Session s", SessionDTO.class);
            return query.getResultList();
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
    public void update(SessionDTO session, SessionDTO updatedSession) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Session existingSession = em.find(Session.class, session.getId());
            if (existingSession == null) {
                throw new ApiException(404, "Session not found");
            }

            List<Exercise> exercises = updatedSession.getExercise().stream()
                    .map(dto -> new Exercise(dto)) // Assuming Exercise has a constructor that accepts ExerciseDTO
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
}