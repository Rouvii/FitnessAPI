package dat.routes;

import dat.config.HibernateConfig;
import dat.controller.ExerciseController;
import dat.dao.ExerciseDAO;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import static io.javalin.apibuilder.ApiBuilder.*;
/**
 * Purpose:
 *
 * @author: Kevin Løvstad Schou
 */
public class ExerciseRoutes {



    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final ExerciseDAO exerciseDAO = new ExerciseDAO(emf);
    private final ExerciseController exerciseController = new ExerciseController(exerciseDAO);
    SecurityController securityController = SecurityController.getInstance();
    public EndpointGroup getRoutes() {
        return () -> {
            before(securityController.authenticate());
            get("/", exerciseController::getAll, Role.USER, Role.ADMIN);
            get("/{id}", exerciseController::getById, Role.USER, Role.ADMIN);
            post("/", exerciseController::create, Role.ADMIN);
            put("/{id}", exerciseController::update, Role.ADMIN);
            delete("/{id}", exerciseController::delete, Role.ADMIN);
        };
    }

}
