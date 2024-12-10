package dat.routes;
import dat.config.HibernateConfig;
import dat.controller.SessionController;
import dat.dao.SessionDAO;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SessionRoutes {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final SessionDAO sessionDAO = new SessionDAO(emf);
    private final SessionController sessionController = new SessionController(sessionDAO);

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", sessionController::getAll);
            get("/{id}", sessionController::getById);
            post("/", sessionController::create);
            put("/{id}", sessionController::update);
            delete("/{id}", sessionController::delete);
        };
    }
}