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

            // Persist sessions with exercises and sets
            for (Session session : sessions) {
                em.persist(session);
                for (Exercise exercise : session.getExercise()) {
                    exercise.setSessions(sessions);
                    em.persist(exercise);
                    for (Set set : exercise.getSets()) {
                        set.setExercise(exercise); // Ensure bi-directional relationship
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

        // Exercise 1
        Exercise exercise1 = new Exercise();
        exercise1.setName("Push Up");
        exercise1.setDescription("Push up exercise");
        exercise1.setMuscleGroup(MuscleGroup.CHEST);

        Set set1 = new Set();
        set1.setReps(10);
        set1.setWeight(0);

        Set set2 = new Set();
        set2.setReps(12);
        set2.setWeight(0);

        exercise1.setSets(List.of(set1, set2));
        set1.setExercise(exercise1);
        set2.setExercise(exercise1);

        // Exercise 2
        Exercise exercise2 = new Exercise();
        exercise2.setName("Pull Up");
        exercise2.setDescription("Pull up exercise");
        exercise2.setMuscleGroup(MuscleGroup.BACK);

        Set set3 = new Set();
        set3.setReps(8);
        set3.setWeight(0);

        exercise2.setSets(List.of(set3));
        set3.setExercise(exercise2);

        // Session 1
        Session session1 = new Session();
        session1.setUser(user);
        session1.setExercise(List.of(exercise1, exercise2));

        return List.of(session1);
    }
}
