package dat.controller;

import dat.dao.ExerciseDAO;
import dat.dao.SessionDAO;
import dat.dto.ExerciseDTO;
import dat.dto.SessionDTO;
import dat.dto.UserDTO;
import dat.entities.Exercise;
import dat.entities.Session;
import dat.exception.ApiException;
import dat.exception.Message;
import dat.security.entities.User;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SessionController implements IController {
    private final SessionDAO sessionDAO;
    private final ExerciseDAO exerciseDAO;
    private final Logger log = LoggerFactory.getLogger(SessionController.class);

    public SessionController(SessionDAO sessionDAO,ExerciseDAO exerciseDAO) {
        this.sessionDAO = sessionDAO;
        this.exerciseDAO = exerciseDAO;
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
        try {
            // Parse sessionDTO fra anmodningen
            SessionDTO sessionDTO = ctx.bodyAsClass(SessionDTO.class);
            UserDTO userDTO = sessionDTO.getUser();

            // Log brugeren for fejlsøgning
            System.out.println("Received UserDTO: " + userDTO);

            // Hent brugeren fra databasen, hvis den eksisterer
            User user = sessionDAO.findUserByUsername(userDTO.getUsername()); // Find brugeren fra DAO

            // Hvis brugeren ikke findes, opret ny bruger
            if (user == null) {
                user = new User(userDTO.getUsername(), userDTO.getPassword());
                sessionDAO.saveOrUpdateUser(user); // Gem den nye bruger
            }

            // Validér brugeren
            if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new ApiException(400, "User credentials are invalid");
            }

            // Opret session og tilknyt brugeren
            // Vi opretter en session med tom liste af øvelser
            Session session = new Session();
            session.setUser(user); // Tilknyt brugeren til sessionen
            session.setExercises(new ArrayList<>()); // Tom liste af øvelser

            System.out.println("Created Session: " + session);

            // Persist sessionen i databasen via DAO
            sessionDAO.create(sessionDTO);  // Brug session entiteten i stedet for DTO

            // Respondér med den oprettede session som en DTO
            SessionDTO responseDTO = new SessionDTO(session); // Map session tilbage til DTO
            ctx.status(201).json(responseDTO);  // Send DTO som JSON

        } catch (ApiException e) {
            // Håndter API-specifikke fejl
            ctx.status(e.getStatusCode()).json(new Message(e.getStatusCode(), e.getMessage()));
        } catch (Exception e) {
            // Log uventede fejl
            ctx.status(500).json(new Message(500, "Internal server error"));
            log.error("Error creating session: {}", e.getMessage());
        }
    }



//    public void create(Context ctx) {
//        try {
//            // Parse sessionDTO fra anmodningen
//            SessionDTO sessionDTO = ctx.bodyAsClass(SessionDTO.class);
//            UserDTO userDTO = sessionDTO.getUser();
//
//            // Log brugeren for fejlsøgning
//            System.out.println("Received UserDTO: " + userDTO);
//
//            // Opret User objekt fra UserDTO
//            User user = new User(userDTO.getUsername(), userDTO.getPassword());
//
//            // Validér brugeren
//            if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
//                throw new ApiException(400, "User credentials are invalid");
//            }
//
//            // Sørg for, at brugeren er vedvarende i databasen
//            sessionDAO.saveOrUpdateUser(user); // Ny metode i DAO-laget
//
//            // Tjek om der er øvelser i sessionDTO
//            List<ExerciseDTO> exercises = sessionDTO.getExercises();
//            if (exercises == null) {
//                exercises = List.of();  // Standard til en tom liste hvis exercises er null
//            }
//
//            // Set exercises (selv tomme)
//            sessionDTO.setExercises(exercises);
//
//            // Opret session og tilknyt brugeren
//            Session session = new Session(sessionDTO);
//            session.setUser(user); // Tilknyt brugeren til sessionen
//            System.out.println("Created Session: " + session);
//
//            // Persist sessionen i databasen via DAO
//            sessionDAO.create(sessionDTO);  // Brug sessionDTO når du kalder create metode
//
//            // Respondér med den oprettede session
//            ctx.status(201).json(session);
//
//        } catch (ApiException e) {
//            // Håndter API-specifikke fejl
//            ctx.status(e.getStatusCode()).json(new Message(e.getStatusCode(), e.getMessage()));
//        } catch (Exception e) {
//            // Log uventede fejl
//        }
//    }


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



    public void addExerciseToSession(Context ctx) {
        try {
            int sessionId = Integer.parseInt(ctx.pathParam("sessionId"));
            int exerciseId = Integer.parseInt(ctx.pathParam("exerciseId"));

            // Fetch the Session and Exercise entities
            Session session = sessionDAO.getSessionEntityById(sessionId);
            Exercise exercise = exerciseDAO.getExerciseEntityById(exerciseId);

            if (session == null || exercise == null) {
                ctx.status(404).json(new Message(404, "Session or Exercise not found"));
                return;
            }

            // Add the Exercise to the Session
            session.addExercise(exercise);

            // Directly update the Session entity
            sessionDAO.updateReal(sessionId, session); // Use the entity, not a DTO

            ctx.status(200).json(new Message(200, "Exercise added to session"));
        } catch (Exception e) {
            log.error("Error adding exercise to session: {}", e.getMessage());
            throw new ApiException(400, e.getMessage());
        }
    }

}
