package Server.Handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Subtask;
import com.yandex.app.service.Interfaces.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager fileBackedTaskManager;

    public SubtasksHandler(TaskManager fileBackedTaskManager) {
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        Endpoint endpoint = getEndpoint(exchange);

        switch (endpoint) {
            case GET_SUBTASKS -> handleGetSubtasks(exchange);
            case GET_SUBTASKS_ID -> handleGetSubtasksId(exchange);
            case POST_SUBTASKS -> handlePostSubtasks(exchange);
            case DELETE_SUBTASKS_ID -> handleDeleteSubtasksId(exchange);
            default -> sendNotFound(exchange, "Данный метод не реализован", 404);
        }
    }

    public void handleGetSubtasks(HttpExchange exchange) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        List<Subtask> subtasks = fileBackedTaskManager.subtasksList();
        if (subtasks.isEmpty()) {
            sendNotFound(exchange, "Список задач пуст", 404);
            return;
        }

        String response = gson.toJson(subtasks, new TaslsListTypeToken().getType());
        sendText(exchange, response);
    }

    public void handleGetSubtasksId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] pathSplit = path.split("/");
        int taskId = Integer.parseInt(pathSplit[2]);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        Subtask subtask = fileBackedTaskManager.getSubtask(taskId);

        if (subtask == null) {
            sendNotFound(exchange, "Такой задачи не существует", 404);
            return;
        }

        String response = gson.toJson(subtask);
        sendText(exchange, response);
    }

    public void handlePostSubtasks(HttpExchange exchange) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        try (InputStream is = exchange.getRequestBody();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Subtask subtask = gson.fromJson(reader, Subtask.class);
            if (subtask == null) {
                sendNotFound(exchange, "Переданная задача пустая", 404);
                return;
            }
            if (fileBackedTaskManager.subtasksList().contains(subtask)) {
                fileBackedTaskManager.updateSubtask(subtask);
                sendText(exchange, "Задача" + subtask.getName() + "обновлена");
                return;
            }
            fileBackedTaskManager.addNewSubtask(subtask);
            if (!(fileBackedTaskManager.getSubtask(subtask.getId()) == null)) {
                sendNotFound(exchange, "Ошибка при добавлении subtask!", 404);
                return;
            }
            sendText(exchange, "Задача" + subtask.getName() + "успешно добавлена в список, присвоен ID =" + subtask.getId());

        } catch (IOException e) {
            sendNotFound(exchange, "Ошибка при добавлении задачи subtask!", 404);
        }
    }

    public void handleDeleteSubtasksId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        int taskId = Integer.parseInt(splitPath[2]);


        if (fileBackedTaskManager.getSubtask(taskId) == null) {
            sendNotFound(exchange, "Такой задачи не существует!", 404);
        }

        fileBackedTaskManager.deleteSubtask(taskId);
        sendText(exchange, "Задача успешно удалена!");
    }

}
