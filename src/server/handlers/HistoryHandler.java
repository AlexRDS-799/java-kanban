package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager fileBackedTaskManager;

    public HistoryHandler(TaskManager fileBackedTaskManager) {
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();
        ArrayList<Task> history = fileBackedTaskManager.getHistory();

        if (history.isEmpty()) {
            sendNotFound(exchange, "История пустая", 400);
            return;
        }

        String response = gson.toJson(history, new TaslsListTypeToken().getType());
        sendText(exchange, response);
    }
}
