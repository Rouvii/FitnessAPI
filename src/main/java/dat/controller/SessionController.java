package dat.controller;

import dat.dao.SessionDAO;
import dat.dto.SessionDTO;
import dat.dto.UserDTO;
import dat.entities.Session;
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
            SessionDTO session = sessionDAO.getById(id);

            if (session == null) {
                ctx.status(404);
                ctx.json(new Message(404, "Session not found"));
                return;
            }

            ctx.status(200);
            ctx.json(session, SessionDTO.class);
        } catch (NumberFormatException e) {
            log.error("Invalid ID format: {}", e.getMessage());
            throw new ApiException(400, "Invalid ID format");
        } catch (Exception e) {
            ctx.json(new Message(400, "Invalid request"));
            log.error("400 {}", e.getMessage());
            throw new ApiException(400, e.getMessage());
        }
    }

    public void create(Context ctx) {
        SessionDTO sessionDTO = ctx.bodyAsClass(SessionDTO.class);
        UserDTO userDTO = sessionDTO.getUser();

        // Convert UserDTO to User
        User user = new User(userDTO.getUsername(), userDTO.getPassword());

        // Ensure the user is persisted
        if (user.getUsername().equals("") || user.getPassword().equals("")) {
            // Save the user entity if it is not already persisted
            sessionDAO.saveUser(user);
        }

        Session session = new Session(sessionDTO);
        session.setUser(user); // Set the user in the session
        sessionDAO.save(session);
        ctx.status(201).json(session);
    }

    @Override
    public void getAll(Context ctx) {
        try {
            List<SessionDTO> sessionDtos = sessionDAO.getAll();

            if (sessionDtos.isEmpty()) {
                ctx.status(404);
                ctx.json(new Message(404, "No sessions found"));
                return;
            }

            ctx.status(200);
            ctx.json(sessionDtos, SessionDTO.class);
        } catch (ApiException e) {
            log.error("Error reading all sessions: {}", e.getMessage());
            ctx.json(new Message(500, "Error reading all sessions"));
            throw new ApiException(500, e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            SessionDTO sessionDto = ctx.bodyAsClass(SessionDTO.class);
            sessionDAO.update(id, sessionDto);
            ctx.status(200);
            ctx.json(new Message(200, "Session updated"));
        } catch (Exception e) {
            log.error("400 {}", e.getMessage());
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            sessionDAO.delete(id);
            ctx.status(200);
            ctx.json(new Message(200, "Session deleted"));
        } catch (Exception e) {
            log.error("400 {}", e.getMessage());
            throw new ApiException(400, e.getMessage());
        }
    }
}