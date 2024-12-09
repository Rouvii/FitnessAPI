package dat.routes;

import static io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.apibuilder.EndpointGroup;

public class Routes {


private final SessionRoutes sessionRoutes = new SessionRoutes();

  public EndpointGroup getRoutes() {
    return () -> {

        path("/sessions", sessionRoutes.getRoutes());

    };
  }



}
