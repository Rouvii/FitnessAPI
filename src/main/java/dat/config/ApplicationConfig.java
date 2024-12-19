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

    // Allow requests from specific origins (e.g., React app on localhost during development)
    private static final String ALLOWED_ORIGIN = "http://localhost:5173"; // Adjust to your React app's URL

    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes", Role.ANYONE);
        config.router.contextPath = "/api"; // base path for all endpoints
        config.router.apiBuilder(routes.getRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());
    }

    public static Javalin startServer(int port) {
        Javalin app = Javalin.create(ApplicationConfig::configuration);
        app.before(ApplicationConfig::corsHeaders);
        app.options("/*", ApplicationConfig::corsHeadersOptions);
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

    // CORS headers to handle cross-origin requests
    private static void corsHeaders(Context ctx) {
        String origin = ctx.header("Origin");

        // Only allow requests from specific origins (e.g., React app during development)
        if (origin != null && (origin.equals(ALLOWED_ORIGIN) || origin.equals("https://your-production-url.com"))) {
            ctx.header("Access-Control-Allow-Origin", origin);
        } else {
            // Default to localhost for local development (remove this if not needed)
            ctx.header("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        }

        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true"); // Allow credentials (cookies, Authorization headers)
    }

    // Handle OPTIONS preflight requests
    private static void corsHeadersOptions(Context ctx) {
        String origin = ctx.header("Origin");

        // Only allow requests from specific origins (e.g., React app during development)
        if (origin != null && (origin.equals(ALLOWED_ORIGIN) || origin.equals("https://your-production-url.com"))) {
            ctx.header("Access-Control-Allow-Origin", origin);
        } else {
            // Default to localhost for local development (remove this if not needed)
            ctx.header("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        }

        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
        ctx.status(204); // No content
    }
}
