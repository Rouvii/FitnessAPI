package dat.config;

import dat.entities.MuscleGroup;
import dat.entities.Session;
import dat.entities.Exercise;
import dat.entities.Set;
import dat.security.entities.User;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Populate {

    public static void populate() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        List<Session> sessions = getSessions();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Persist the user
            User user = sessions.get(0).getUser();
            em.persist(user);

            // Persist sessions
            for (Session session : sessions) {
                em.persist(session);
                // Persist exercises and sets after setting the session
                for (Exercise exercise : session.getExercise()) {
                    exercise.setSession(session);
                    em.persist(exercise);
                    for (Set set : exercise.getSets()) {
                        em.persist(set);
                    }
                }
            }

            em.getTransaction().commit();
        }
    }

    private static List<Session> getSessions() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("password");

        Exercise exercise1 = new Exercise();
        exercise1.setName("Push Up");
        exercise1.setDescription("Push up exercise");
        exercise1.setMuscleGroup(MuscleGroup.CHEST);

        Set set1 = new Set();
        set1.setReps(10);
        set1.setWeight(0);
        set1.setExercise(exercise1);

        exercise1.setSets(List.of(set1));

        Session session1 = new Session();
        session1.setUser(user);
        session1.setExercise(List.of(exercise1));

        return List.of(session1);
    }
}