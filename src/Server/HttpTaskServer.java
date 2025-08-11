package Server;

import Server.Handlers.*;
import com.sun.net.httpserver.HttpServer;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {

        String pathTasks = "C:\\Users\\Alexandr\\IdeaProjects\\java-kanban\\src\\com\\yandex\\app" +
                "\\service\\File_Backed\\SavedManager\\SavedTasks.txt";
        String pathHistory = "C:\\Users\\Alexandr\\IdeaProjects\\java-kanban\\src\\com\\yandex\\app" +
                "\\service\\File_Backed\\SavedManager\\SavedHistory.txt";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TaskManager fileBackedTaskManager = Managers.getFileBackedManager(pathTasks, pathHistory);


        Task task3 = new Task("Task2", "second task");

        task3.setDuration(Duration.ofMinutes(50));
        task3.setStartTime(LocalDateTime.parse("2025-10-10 12:00:00", formatter));
//
//        fileBackedTaskManager.addNewTask(task1);
//        fileBackedTaskManager.addNewTask(task2);

        //==============СЕРВЕР===========================
        String uriStr = "http://localhost:8080";
        URI url = URI.create(uriStr + "/tasks");
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        System.out.println(fileBackedTaskManager.tasksList());
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

        System.out.println("Сервер запущен на " +PORT+" порту");
        server.start();

        //===============================================

    }

}

