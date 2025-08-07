package Server;

import Server.Handlers.BaseHttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Task;
import com.yandex.app.model.Subtask;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {

        String pathTasks = "C:\\Users\\Alexandr\\IdeaProjects\\java-kanban\\src\\com\\yandex\\app" +
                "\\service\\File_Backed\\SavedManager\\SavedTasks.txt";
        String pathHistory = "C:\\Users\\Alexandr\\IdeaProjects\\java-kanban\\src\\com\\yandex\\app" +
                "\\service\\File_Backed\\SavedManager\\SavedHistory.txt";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TaskManager fileBackedTaskManager = Managers.getFileBackedManager(pathTasks, pathHistory);

      //==========Добавляем задачи================
//        Task task1 = new Task("Task1", "first task");
//        Task task2 = new Task("Task2", "second task");
//        task1.setDuration(Duration.ofMinutes(10));
//        task1.setStartTime(LocalDateTime.parse("2025-07-15 12:00:00", formatter));
//
//        task2.setDuration(Duration.ofMinutes(50));
//        task2.setStartTime(LocalDateTime.parse("2025-07-10 12:00:00", formatter));
//
//        fileBackedTaskManager.addNewTask(task1);
//        fileBackedTaskManager.addNewTask(task2);
//        Epic epic = new Epic("Epic1", "first epic");
//        fileBackedTaskManager.addNewEpic(epic);
//
//        fileBackedTaskManager.getTask(task1.getId());
//
//        Subtask subtask1 = new Subtask("Subtask1", "first subtask", epic.getId());
//        Subtask subtask2 = new Subtask("Subtask2", "second subtask", epic.getId());
//        Subtask subtask3 = new Subtask("Subtask3", "third subtask", epic.getId());
//
//
//        subtask1.setDuration(Duration.ofMinutes(40));
//        subtask1.setStartTime(LocalDateTime.parse("2025-07-17 15:20:00", formatter));
//
//        subtask2.setDuration(Duration.ofMinutes(10));
//        subtask2.setStartTime(LocalDateTime.parse("2025-07-17 15:30:00", formatter));
//
//        subtask3.setDuration(Duration.ofMinutes(80));
//        subtask3.setStartTime(LocalDateTime.parse("2025-07-19 12:40:00", formatter));
//
//
//        fileBackedTaskManager.addNewSubtask(subtask1);
//        fileBackedTaskManager.addNewSubtask(subtask2);
//        fileBackedTaskManager.addNewSubtask(subtask3);
      //=============================================


        //==============СЕРВЕР===========================
        List<Task> tasks = fileBackedTaskManager.tasksList();
        List<Task> subtasks = new ArrayList<>(fileBackedTaskManager.subtasksList());
        List<Task> epics = new ArrayList<>(fileBackedTaskManager.epicsList());
        System.out.println(tasks);
        System.out.println(subtasks);
        String uriStr = "http://localhost:8080";
        URI url = URI.create(uriStr + "/tasks");
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new BaseHttpHandler(tasks));
        server.createContext("/subtasks", new BaseHttpHandler(subtasks));
        server.createContext("/epics", new BaseHttpHandler(epics));
        System.out.println("Сервер запущен на " +PORT+" порту");
        server.start();
        //===============================================

    }

}

