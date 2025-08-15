package Server;

import Server.Handlers.*;
import com.sun.net.httpserver.HttpServer;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

        String pathTasks = "C:\\Users\\Alexandr\\IdeaProjects\\java-kanban\\src\\com\\yandex\\app" +
                "\\service\\File_Backed\\SavedManager\\SavedTasks.txt";
        String pathHistory = "C:\\Users\\Alexandr\\IdeaProjects\\java-kanban\\src\\com\\yandex\\app" +
                "\\service\\File_Backed\\SavedManager\\SavedHistory.txt";
        TaskManager fileBackedTaskManager = Managers.getFileBackedManager(pathTasks, pathHistory);
        System.out.println(fileBackedTaskManager.getPrioritizedTasks());
        //==============СЕРВЕР===========================
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
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

        System.out.println("Сервер запущен на " + PORT + " порту");
        server.start();
        //===============================================


    }

}

