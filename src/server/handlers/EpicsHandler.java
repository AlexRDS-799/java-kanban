package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Epic;
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

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager fileBackedTaskManager;

    public EpicsHandler(TaskManager fileBackedTaskManager) {
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        switch (endpoint) {
            case GET_EPICS -> handleGetEpics(exchange);
            case GET_EPICS_ID -> handleGetEpicsId(exchange);
            case GET_EPICS_ID_SUBTASKS -> handleGetEpicsIdSubtasks(exchange);
            case POST_EPICS -> handlePostEpics(exchange);
            case DELETE_EPICS_ID -> handleDeleteEpicsId(exchange);
            default -> sendNotFound(exchange, "Данный метод не реализован", 404);
        }
    }

    public void handleGetEpics(HttpExchange exchange) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        List<Epic> epics = fileBackedTaskManager.epicsList();

        if (epics.isEmpty()) {
            sendNotFound(exchange, "Список задач пуст!", 404);
            return;
        }

        String response = gson.toJson(epics, new TaslsListTypeToken().getType()); //настроить json чтобы не выводил сразу список сабтасков
        sendText(exchange, response);

    }

    public void handleGetEpicsId(HttpExchange exchange) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");

        int taskId = Integer.parseInt(splitPath[2]);

        Epic epic = fileBackedTaskManager.getEpic(taskId);

        if (epic == null) {
            sendNotFound(exchange, "Такого epic не существует!", 404);
            return;
        }

        String response = gson.toJson(epic);
        sendText(exchange, response);

    }

    public void handleGetEpicsIdSubtasks(HttpExchange exchange) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        int epicId = Integer.parseInt(splitPath[2]);

        Epic epic = fileBackedTaskManager.getEpic(epicId);
        if (epic == null) {
            sendNotFound(exchange, "Такого эпика не существует!", 404);
            return;
        }

        List<Subtask> subtasksInEpic = epic.getSubtasksInThisEpic();

        if (subtasksInEpic.isEmpty()) {
            sendText(exchange, "Список подзадач для " + epic.getName() + " пуст!");
            return;
        }

        String response = gson.toJson(subtasksInEpic, new TaslsListTypeToken().getType());
        sendText(exchange, response);
    }

    public void handlePostEpics(HttpExchange exchange) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        try (InputStream is = exchange.getRequestBody();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Epic epic = gson.fromJson(reader, Epic.class);

            if (epic == null) {
                sendNotFound(exchange, "Переданная задача пустая", 404);
                return;
            }

            if (fileBackedTaskManager.epicsList().contains(epic)) {
                fileBackedTaskManager.updateTask(epic);
                sendText(exchange, "Задача" + epic.getName() + "обновлена");
                return;
            }
            fileBackedTaskManager.addNewEpic(epic);
            if (fileBackedTaskManager.getEpic(epic.getId()) == null) {
                sendNotFound(exchange, "Ошибка при добавлении epic!", 404);
                return;
            }
            sendText(exchange, "Задача" + epic.getName() + "успешно добавлена в список, присвоен ID =" + epic.getId());
        } catch (IOException e) {
            sendNotFound(exchange, "Ошибка при чтении запроса!", 404);
        }
    }

    public void handleDeleteEpicsId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        int epicId = Integer.parseInt(splitPath[2]);


        if (fileBackedTaskManager.getTask(epicId) == null) {
            sendNotFound(exchange, "Такой задачи не существует!", 404);
        }

        fileBackedTaskManager.deleteEpic(epicId);
        sendText(exchange, "Задача успешно удалена!");
    }
}
