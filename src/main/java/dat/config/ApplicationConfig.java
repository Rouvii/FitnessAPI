package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.routes.Routes;
import dat.security.controllers.AccessController;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import dat.security.exceptions.ApiException;
import dat.security.routes.SecurityRoutes;
import dat.util.Utils;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static Routes routes = new Routes();
    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();
    private static SecurityController securityController = SecurityController.getInstance();
    private static AccessController accessController = new AccessController();
    private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes", Role.ANYONE);
        config.router.contextPath = "/api"; // Base path for all endpoints
        config.router.apiBuilder(routes.getRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());
    }

    public static Javalin startServer(int port) {
        Javalin app = Javalin.create(ApplicationConfig::configuration);
        app.before(ApplicationConfig::corsHeaders); // Apply CORS headers to every request
        app.options("/*", ApplicationConfig::corsHeadersOptions); // Handle preflight OPTIONS requests
        app.beforeMatched(accessController::accessHandler);

        app.exception(ApiException.class, ApplicationConfig::apiExceptionHandler);
        app.exception(Exception.class, ApplicationConfig::generalExceptionHandler);

        app.start(port);
        return app;
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }

    private static void generalExceptionHandler(Exception e, Context ctx) {
        logger.error("An unhandled exception occurred", e.getMessage());
        ctx.json(Utils.convertToJsonMessage(ctx, "error", e.getMessage()));
    }

    public static void apiExceptionHandler(ApiException e, Context ctx) {
        ctx.status(e.getCode());
        logger.warn("An API exception occurred: Code: {}, Message: {}", e.getCode(), e.getMessage());
        ctx.json(Utils.convertToJsonMessage(ctx, "warning", e.getMessage()));
    }

    private static void corsHeaders(Context ctx) {
        String origin = ctx.header("Origin"); // Dynamically set the allowed origin
        if (origin != null) {
            ctx.header("Access-Control-Allow-Origin", origin); // Reflect the origin header from the request
        } else {
            ctx.header("Access-Control-Allow-Origin", "https://your-frontend-domain.com"); // Fallback origin
        }
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
        ctx.header("Access-Control-Max-Age", "86400"); // Cache preflight responses for 24 hours
    }

    private static void corsHeadersOptions(Context ctx) {
        String origin = ctx.header("Origin");
        if (origin != null) {
            ctx.header("Access-Control-Allow-Origin", origin); // Reflect the origin
        } else {
            ctx.header("Access-Control-Allow-Origin", "https://your-frontend-domain.com"); // Fallback origin
        }
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
        ctx.header("Access-Control-Max-Age", "86400"); // Cache preflight responses for 24 hours
        ctx.status(204); // No content for preflight responses
    }
}
