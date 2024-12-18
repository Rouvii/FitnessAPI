package dat.controller;

import dat.dao.IDao;
import dat.entities.Exercise;
import dat.entities.Session;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class SessionController implements IController {

    private final IDao<Session> sessionDao;
    private final IDao<Exercise> exerciseDao;

    public SessionController(IDao<Session> sessionDao, IDao<Exercise> exerciseDao) {
        this.sessionDao = sessionDao;
        this.exerciseDao = exerciseDao;
    }

    @Override
    public void create(Context ctx) {
        try {
            // Parse the Session object from the request body
            Session session = ctx.bodyAsClass(Session.class);

            // Initialize the session's exercises list as empty
            session.setExercises(List.of());  // Set an empty list for exercises initially

            // Ensure session is created properly (don't pass "id", Hibernate will generate it)
            session.setId(0);  // Ensure ID is not set manually; Hibernate will handle it

            // Save the session with an empty exercises list
            session = sessionDao.create(session);
            ctx.status(201).json(session);  // Respond with the created session
        } catch (Exception e) {
            ctx.status(400).result("Failed to create session: " + e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Session session = ctx.bodyAsClass(Session.class);

            // Fetch the existing session from the database
            Session existingSession = sessionDao.getById(id);
            if (existingSession == null) {
                ctx.status(404).result("Session not found");
                return;
            }

            // Fetch exercises for the session from the database
            List<Exercise> exercises = session.getExercises().stream()
                    .map(exercise -> exerciseDao.getById(exercise.getId())) // Fetch each exercise by ID
                    .filter(exercise -> exercise != null) // Ensure exercises are valid
                    .collect(Collectors.toList());

            if (!exercises.isEmpty()) {
                existingSession.setExercises(exercises); // Set the new exercises to the existing session
            }

            // Update the session in the database
            sessionDao.update(id, existingSession);
            ctx.result("Session updated successfully");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid ID format");
        } catch (Exception e) {
            ctx.status(400).result("Failed to update session: " + e.getMessage());
        }
    }

    @Override
    public void getAll(Context ctx) {
        List<Session> sessions = sessionDao.getAll();
        ctx.json(sessions);
    }

    @Override
    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Session session = sessionDao.getById(id);
            if (session == null) {
                ctx.status(404).result("Session not found");
            } else {
                ctx.json(session);
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid ID format");
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            sessionDao.delete(id);
            ctx.result("Session deleted successfully");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid ID format");
        } catch (Exception e) {
            ctx.status(400).result("Failed to delete session: " + e.getMessage());
        }
    }

    public void addExercises(Context ctx) {
        try {
            int sessionId = Integer.parseInt(ctx.pathParam("id"));
            List<Integer> exerciseIds = ctx.bodyAsClass(List.class);

            List<Exercise> exercises = exerciseIds.stream()
                    .map(exerciseId -> exerciseDao.getById(exerciseId))
                    .filter(exercise -> exercise != null)
                    .collect(Collectors.toList());

            if (exercises.isEmpty()) {
                ctx.status(400).result("No valid exercises to add");
                return;
            }

            // Fetch the session to which we want to add exercises
            Session session = sessionDao.getById(sessionId);
            if (session == null) {
                ctx.status(404).result("Session not found");
                return;
            }

            // Add exercises to the session
            session.getExercises().addAll(exercises);

            // Update the session in the database
            sessionDao.update(sessionId, session);
            ctx.result("Exercises added successfully");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid ID format");
        } catch (Exception e) {
            ctx.status(400).result("Failed to add exercises to session: " + e.getMessage());
        }
    }
}
