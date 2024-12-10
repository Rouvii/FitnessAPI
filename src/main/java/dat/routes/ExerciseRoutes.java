package dat.routes;

import dat.config.HibernateConfig;
import dat.controller.ExerciseController;
import dat.dao.ExerciseDAO;
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

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", exerciseController::getAll);
            get("/{id}", exerciseController::getById);
            post("/", exerciseController::create);
            put("/{id}", exerciseController::update);
            delete("/{id}", exerciseController::delete);
        };
    }

}
