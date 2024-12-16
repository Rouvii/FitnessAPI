package dat.routes;
import dat.config.HibernateConfig;
import dat.controller.SessionController;
import dat.dao.SessionDAO;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SessionRoutes {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final SessionDAO sessionDAO = new SessionDAO(emf);
    private final SessionController sessionController = new SessionController(sessionDAO);
    SecurityController securityController = SecurityController.getInstance();

    public EndpointGroup getRoutes() {
        return () -> {
            before(securityController.authenticate());
            get("/", sessionController::getAll, Role.ADMIN);
            get("/{id}", sessionController::getById, Role.USER, Role.ADMIN);
            post("/", sessionController::create,Role.USER, Role.ADMIN);
            put("/{id}", sessionController::update, Role.ADMIN);
            delete("/{id}", sessionController::delete, Role.USER);
        };
    }
}