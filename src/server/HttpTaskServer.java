package server;

import server.handlers.*;
import com.sun.net.httpserver.HttpServer;
import com.yandex.app.service.Interfaces.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    static TaskManager filedBackedTaskManager;
    static HttpServer server;

    public HttpTaskServer(TaskManager taskManager) {
        this.filedBackedTaskManager = taskManager;
        try {
            this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            String errorMessage = "Ошибка при создании сервера: " + e.getMessage();
            throw new RuntimeException(errorMessage, e);
        }
    }


    public static void main(String[] args) throws IOException {
        //для использования методов start stop в тестах, все методы сделал public, поэтому они пока
        //не используются в main методе
    }

    public void start() throws IOException {
        System.out.println("Сервер запущен на " + PORT + " порту");
        createAllContexts(server, filedBackedTaskManager);
        server.start();
    }

    public static void createAllContexts(HttpServer server, TaskManager fileBackedTaskManager) {
        //TASKS
        TasksHandler tasksHandler = new TasksHandler(fileBackedTaskManager);
        server.createContext("/tasks", tasksHandler);
        server.createContext("/tasks/{id}", tasksHandler);
        //SUBTASKS
        SubtasksHandler subtasksHandler = new SubtasksHandler(fileBackedTaskManager);
        server.createContext("/subtasks", subtasksHandler);
        server.createContext("/subtasks/{id}", subtasksHandler);
        //EPICS
        EpicsHandler epicsHandler = new EpicsHandler(fileBackedTaskManager);
        server.createContext("/epics", epicsHandler);
        server.createContext("/epics/{id}", epicsHandler);
        server.createContext("/epics/{id}/subtasks", epicsHandler);
        //HISTORY
        HistoryHandler historyHandler = new HistoryHandler(fileBackedTaskManager);
        server.createContext("/history", historyHandler);
        //PRIORITIZED
        PrioritizedHandler prioritizedHandler = new PrioritizedHandler(fileBackedTaskManager);
        server.createContext("/prioritized", prioritizedHandler);
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен");
    }
}

