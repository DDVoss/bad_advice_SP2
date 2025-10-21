package app.routes;

import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final AdviceRoute adviceRoute = new AdviceRoute();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/advice", adviceRoute.getRoutes());
        };
    }
}
