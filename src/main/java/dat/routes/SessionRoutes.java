package dat.routes;
import dat.config.HibernateConfig;
import dat.controller.SessionController;
import dat.dao.ExerciseDAO;
import dat.dao.SessionDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SessionRoutes {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final SessionDAO sessionDAO = new SessionDAO(emf);
    private final ExerciseDAO exerciseDAO = new ExerciseDAO(emf);
    private final SessionController sessionController = new SessionController(sessionDAO, exerciseDAO);

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", sessionController::getAll);
            get("/{id}", sessionController::getById);
            post("/", sessionController::create);
            put("/{id}", sessionController::update);
            delete("/{id}", sessionController::delete);
            put("/addExerciseToSession/{sessionId}/{exerciseId}", sessionController::addExerciseToSession);
        };
    }
}