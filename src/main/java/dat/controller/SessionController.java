package dat.controller;

import dat.dao.SessionDAO;
import dat.dto.SessionDTO;
import dat.dto.UserDTO;
import dat.entities.Session;
import dat.entities.Exercise;
import dat.exception.ApiException;
import dat.exception.Message;
import dat.security.entities.User;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SessionController implements IController {

    private final SessionDAO sessionDAO;
    private final Logger log = LoggerFactory.getLogger(SessionController.class);

    public SessionController(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            SessionDTO sessionDTO = sessionDAO.getById(id);

            if (sessionDTO == null) {
                ctx.status(404).json(new Message(404, "Session not found"));
                return;
            }

            ctx.status(200).json(sessionDTO);
        } catch (NumberFormatException e) {
            log.error("Invalid ID format: {}", e.getMessage());
            throw new ApiException(400, "Invalid session ID format");
        } catch (Exception e) {
            log.error("Error fetching session: {}", e.getMessage());
            throw new ApiException(500, "Error fetching session");
        }
    }

    public void create(Context ctx) {
        try {
            // Parse session DTO from request body
            SessionDTO sessionDTO = ctx.bodyAsClass(SessionDTO.class);
            UserDTO userDTO = sessionDTO.getUser();

            if (userDTO == null || userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
                throw new ApiException(400, "User information is missing or invalid");
            }

            // Fetch or create the user
            User user = sessionDAO.findUserByUsername(userDTO.getUsername());
            if (user == null) {
                user = new User(userDTO.getUsername(), userDTO.getPassword());
                sessionDAO.saveUser(user);
            }

            // Map exercise IDs to Exercise entities
            List<Integer> exerciseIds = sessionDTO.getExerciseIds();
            List<Exercise> exercises = sessionDAO.findExercisesByIds(exerciseIds);

            // Create and persist the session
            Session session = new Session();
            session.setName(sessionDTO.getName());
            session.setUser(user);
            session.setExercise(exercises);

            sessionDAO.create(new SessionDTO(session));

            ctx.status(201).json(new Message(201, "Session created successfully"));
        } catch (ApiException e) {
            log.error("Error creating session: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error creating session: {}", e.getMessage());
            throw new ApiException(500, "Unexpected error occurred while creating session");
        }
    }

    @Override
    public void getAll(Context ctx) {
        try {
            List<SessionDTO> sessionDTOs = sessionDAO.getAll();

            if (sessionDTOs.isEmpty()) {
                ctx.status(404).json(new Message(404, "No sessions found"));
                return;
            }

            ctx.status(200).json(sessionDTOs);
        } catch (Exception e) {
            log.error("Error fetching all sessions: {}", e.getMessage());
            throw new ApiException(500, "Error fetching all sessions");
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            SessionDTO updatedSessionDTO = ctx.bodyAsClass(SessionDTO.class);

            // Validate input data
            if (updatedSessionDTO == null || updatedSessionDTO.getExerciseIds() == null) {
                throw new ApiException(400, "Invalid session data");
            }

            // Perform update
            sessionDAO.update(id, updatedSessionDTO);

            ctx.status(200).json(new Message(200, "Session updated successfully"));
        } catch (NumberFormatException e) {
            log.error("Invalid session ID: {}", e.getMessage());
            throw new ApiException(400, "Invalid session ID format");
        } catch (Exception e) {
            log.error("Error updating session: {}", e.getMessage());
            throw new ApiException(500, "Error updating session");
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            sessionDAO.delete(id);

            ctx.status(200).json(new Message(200, "Session deleted successfully"));
        } catch (NumberFormatException e) {
            log.error("Invalid session ID: {}", e.getMessage());
            throw new ApiException(400, "Invalid session ID format");
        } catch (ApiException e) {
            log.error("Error deleting session: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error deleting session: {}", e.getMessage());
            throw new ApiException(500, "Error deleting session");
        }
    }
}
