import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import com.yandex.app.service.In_Memory.InMemoryTaskManager;
import com.yandex.app.service.Interfaces.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.TaskTrackerAPI;

import java.io.*;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    TaskTrackerAPI taskTrackerAPI = new TaskTrackerAPI();

    @BeforeEach
    void startServer() throws IOException {
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();
        taskManager.clearAllSubtasks();
        taskServer.start();

    }

    @AfterEach
    void stopServer() {
        taskServer.stop();
    }


    @Test
    void addTasksTest() {
        Task task = new Task("Task1", "Description1");
        task.setDuration(Duration.ofMinutes(10));
        task.setStartTime(LocalDateTime.parse("2025-07-15 12:00:00", formatter));

        HttpResponse<String> responseAddTask = taskTrackerAPI.addTask(task);
        assertEquals(200, responseAddTask.statusCode());

        Task task2 = new Task("Task2", "Description2");
        task2.setDuration(Duration.ofMinutes(10));
        task2.setStartTime(LocalDateTime.parse("2025-07-24 12:00:00", formatter));

        HttpResponse<String> responseAddTask2 = taskTrackerAPI.addTask(task2);
        assertEquals(200, responseAddTask2.statusCode());


    }

    @Test
    void addEpicSubtaskTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "DescriptionEpic1");
        epic.setDuration(Duration.ofMinutes(20));
        epic.setStartTime(LocalDateTime.parse("2025-07-20 12:00:00", formatter));

        HttpResponse<String> responseAddEpic = taskTrackerAPI.addEpic(epic);
        assertEquals(200, responseAddEpic.statusCode());


        Subtask subtask1 = new Subtask("subtask1", "DescriptionSubtask1", 1);
        subtask1.setDuration(Duration.ofMinutes(10));
        subtask1.setStartTime(LocalDateTime.parse("2025-07-21 10:00:00", formatter));

        HttpResponse<String> responseAddSubtask = taskTrackerAPI.addSubtask(subtask1);
        assertEquals(200, responseAddSubtask.statusCode());

    }


    @Test
    void getTasksTest() throws IOException, InterruptedException {
        HttpResponse<String> response = taskTrackerAPI.getTasks();
        assertEquals(200, response.statusCode());
    }

    @Test
    void getTasksIdTest() throws IOException, InterruptedException {
        Task task = new Task("Task1", "Description1");
        task.setDuration(Duration.ofMinutes(10));
        task.setStartTime(LocalDateTime.parse("2025-07-15 12:00:00", formatter));
        taskManager.addNewTask(task);

        HttpResponse<String> response = taskTrackerAPI.getTasksId(task.getId());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getEpicsTest() throws IOException, InterruptedException {
        HttpResponse<String> response = taskTrackerAPI.getEpic();
        assertEquals(200, response.statusCode());
    }

    @Test
    void getEpicsIdTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "DescriptionEpic1");
        epic.setDuration(Duration.ofMinutes(20));
        epic.setStartTime(LocalDateTime.parse("2025-07-20 12:00:00", formatter));
        taskManager.addNewEpic(epic);

        HttpResponse<String> response = taskTrackerAPI.getEpicId(epic.getId());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getEpicsIdSubtasksTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "DescriptionEpic1");
        epic.setDuration(Duration.ofMinutes(20));
        epic.setStartTime(LocalDateTime.parse("2025-07-20 12:00:00", formatter));
        taskManager.addNewEpic(epic);

        Subtask subtask1 = new Subtask("subtask1", "DescriptionSubtask1", epic.getId());
        subtask1.setDuration(Duration.ofMinutes(10));
        subtask1.setStartTime(LocalDateTime.parse("2025-07-21 10:00:00", formatter));
        taskManager.addNewSubtask(subtask1);

        HttpResponse<String> response = taskTrackerAPI.getEpicsIdSubtasks(epic.getId());
        assertEquals(200, response.statusCode());

        HttpResponse<String> responseAddSubtask = taskTrackerAPI.getSubtasksId(subtask1.getId());
        assertEquals(200, responseAddSubtask.statusCode());
    }

    @Test
    void getSubtasksTest() throws IOException, InterruptedException {
        HttpResponse<String> response = taskTrackerAPI.getSubtasks();
        assertEquals(200, response.statusCode());
    }

}
