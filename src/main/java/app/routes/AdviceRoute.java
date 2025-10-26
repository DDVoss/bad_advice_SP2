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
            get("/random", adviceController::getRandomAdvice);
            delete("/wipe", adviceController::wipeAdvices, Role.ADMIN);
            post("/create", adviceController::create, Role.USER);
            get("/", adviceController::readAll);
            get("/{id}", adviceController::read);
            put("/{id}", adviceController::update);
            delete("/delete/{id}", adviceController::delete, Role.USER);
        };
    }
}
