package Server.Handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private static TaskManager fileBackedTaskManager;

    public PrioritizedHandler(TaskManager fileBackedTaskManager) {
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        Set<Task> tasks = fileBackedTaskManager.getPrioritizedTasks();

        if (tasks.isEmpty()) {
            sendText(exchange, "Список задач по приоритету пуст!");
        }
        String response = gson.toJson(tasks, new TaslsListTypeToken().getType());
        sendText(exchange, response);
    }

}
