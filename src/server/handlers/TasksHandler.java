package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.TaskManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager fileBackedTaskManager;

    public TasksHandler(TaskManager fileBackedTaskManager) {
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        Endpoint endpoint = getEndpoint(exchange);

        switch (endpoint) {
            case GET_TASKS -> handleGetTasks(exchange);
            case GET_TASKS_ID -> handleGetTasksId(exchange);
            case POST_TASKS -> handlePostTasks(exchange);
            case DELETE_TASKS_ID -> handleDeleteTasksId(exchange);
            default -> sendNotFound(exchange, "Данный метод не реализован", 404);
        }

    }

    public void handleGetTasks(HttpExchange exchange) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        List<Task> tasks = fileBackedTaskManager.tasksList();
        if (tasks.isEmpty()) {
            sendNotFound(exchange, "Список задач пуст", 404);
            return;
        }

        String response = gson.toJson(tasks, new TaslsListTypeToken().getType());
        sendText(exchange, response);
    }

    public void handleGetTasksId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] pathSplit = path.split("/");
        int taskId = Integer.parseInt(pathSplit[2]);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        Task task = fileBackedTaskManager.getTask(taskId);

        if (task == null) {
            sendNotFound(exchange, "Такой задачи не существует", 404);
            return;
        }

        String response = gson.toJson(task);
        sendText(exchange, response);
    }

    public void handlePostTasks(HttpExchange exchange) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        try (InputStream is = exchange.getRequestBody();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Task task = gson.fromJson(reader, Task.class);
            if (task == null) {
                sendNotFound(exchange, "Переданная задача пустая", 404);
                return;
            }
            if (fileBackedTaskManager.tasksList().contains(task)) {
                fileBackedTaskManager.updateTask(task);
                sendText(exchange, "Задача" + task.getName() + "обновлена");
                return;
            }
            fileBackedTaskManager.addNewTask(task);
            if (!(fileBackedTaskManager.getTask(task.getId()) == null)) {
                sendNotFound(exchange, "Ошибка при добавлении task!", 404);
                return;
            }
            sendText(exchange, "Задача" + task.getName() + "успешно добавлена в список, присвоен ID =" + task.getId());

        } catch (IOException e) {
            sendNotFound(exchange, "Ошибка при добавлении задачи task!", 404);
        }
    }

    public void handleDeleteTasksId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        int taskId = Integer.parseInt(splitPath[2]);


        if (fileBackedTaskManager.getTask(taskId) == null) {
            sendNotFound(exchange, "Такой задачи не существует!", 404);
        }

        fileBackedTaskManager.deleteTask(taskId);
        sendText(exchange, "Задача успешно удалена!");
    }
}
