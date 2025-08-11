package Server.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.service.Interfaces.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
        private static TaskManager fileBackedTaskManager;
    public PrioritizedHandler(TaskManager fileBackedTaskManager) {
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    }

    public void handleGetPrioritized(HttpExchange exchange){

    }
}
