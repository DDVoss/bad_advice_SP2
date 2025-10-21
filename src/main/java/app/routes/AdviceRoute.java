package app.routes;

import app.controllers.impl.AdviceController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AdviceRoute {

    private final AdviceController adviceController = new AdviceController();

    protected EndpointGroup getRoutes() {
        return () -> {
            get("/populate", adviceController::populate);
            post("/", adviceController::create, Role.USER);
            get("/", adviceController::readAll);
            get("/{id}", adviceController::read);
            put("/{id}", adviceController::update);
            delete("/{id}", adviceController::delete);
        };
    }
}
