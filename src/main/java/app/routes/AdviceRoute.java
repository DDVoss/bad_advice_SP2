package app.routes;

import app.controllers.impl.AdviceController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AdviceRoute {

    private final AdviceController adviceController = new AdviceController();

    protected EndpointGroup getRoutes() {
        return () -> {
            // Specialized Endpoints
            delete("/wipe", adviceController::wipeAdvices, Role.ADMIN);
            get("/random", adviceController::getRandomAdvice);

            // CRUD Endpoints
            post("/populate", adviceController::populate);
            post("/", adviceController::create, Role.USER);
            get("/", adviceController::readAll);

            // Read update delete by ID
            get("/{id}", adviceController::read);
            put("/{id}", adviceController::update);
            delete("/{id}", adviceController::delete, Role.USER);
        };
    }
}
