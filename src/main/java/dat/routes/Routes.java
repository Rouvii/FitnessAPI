package dat.routes;

import static io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.apibuilder.EndpointGroup;

public class Routes {


private final SessionRoutes sessionRoutes = new SessionRoutes();
private final ExerciseRoutes exerciseRoutes = new ExerciseRoutes();
  public EndpointGroup getRoutes() {
    return () -> {

        path("/sessions", sessionRoutes.getRoutes());
        path("/exercises", exerciseRoutes.getRoutes());
    };
  }



}
