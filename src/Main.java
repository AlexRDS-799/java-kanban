
import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {

        String pathTasks = "C:\\Users\\Alexandr\\IdeaProjects\\java-kanban\\src\\com\\yandex\\app" +
                "\\service\\File_Backed\\SavedManager\\SavedTasks.txt";
        String pathHistory = "C:\\Users\\Alexandr\\IdeaProjects\\java-kanban\\src\\com\\yandex\\app" +
                "\\service\\File_Backed\\SavedManager\\SavedHistory.txt";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TaskManager fileBackedTaskManager = Managers.getFileBackedManager(pathTasks, pathHistory);

        Task task1 = new Task("Task1", "first task");
        Task task2 = new Task("Task2", "second task");
        task1.setDuration(Duration.ofMinutes(10));
        task1.setStartTime(LocalDateTime.parse("2025-07-15 12:00:00", formatter));

        task2.setDuration(Duration.ofMinutes(50));
        task2.setStartTime(LocalDateTime.parse("2025-07-10 12:00:00", formatter));

        fileBackedTaskManager.addNewTask(task1);
        fileBackedTaskManager.addNewTask(task2);
        Epic epic = new Epic("Epic1", "first epic");
        fileBackedTaskManager.addNewEpic(epic);

        fileBackedTaskManager.getTask(task1.getId());

        Subtask subtask1 = new Subtask("Subtask1", "first subtask", epic.getId());
        Subtask subtask2 = new Subtask("Subtask2", "second subtask", epic.getId());
        Subtask subtask3 = new Subtask("Subtask3", "third subtask", epic.getId());


        subtask1.setDuration(Duration.ofMinutes(40));
        subtask1.setStartTime(LocalDateTime.parse("2025-07-17 15:20:00", formatter));

        subtask2.setDuration(Duration.ofMinutes(10));
        subtask2.setStartTime(LocalDateTime.parse("2025-07-17 15:30:00", formatter));

        subtask3.setDuration(Duration.ofMinutes(80));
        subtask3.setStartTime(LocalDateTime.parse("2025-07-19 12:40:00", formatter));


        fileBackedTaskManager.addNewSubtask(subtask1);
        fileBackedTaskManager.addNewSubtask(subtask2);
        fileBackedTaskManager.addNewSubtask(subtask3);

        System.out.println("Список задач по приоритету: " + fileBackedTaskManager.getPrioritizedTasks());
        System.out.println("tasks: " + fileBackedTaskManager.tasksList());
        System.out.println("epics: " + fileBackedTaskManager.epicsList());
        System.out.println("subtasks: " + fileBackedTaskManager.subtasksList());
        System.out.println("history: " + fileBackedTaskManager.getHistory());

        fileBackedTaskManager.clearAllEpics();

        System.out.println("Список после удаления всех епик: ");
        System.out.println("Список задач по приоритету: " + fileBackedTaskManager.getPrioritizedTasks());
        System.out.println("tasks: " + fileBackedTaskManager.tasksList());
        System.out.println("epics: " + fileBackedTaskManager.epicsList());
        System.out.println("subtasks: " + fileBackedTaskManager.subtasksList());
        System.out.println("history: " + fileBackedTaskManager.getHistory());
//        System.out.println("Hello world!");
//        System.out.println("Hello world!");
//        System.out.println("Hello world!");
//
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        subtask1.setStartTime(LocalDateTime.parse("2025-07-18 17:05:00", dateTimeFormatter));
//        subtask1.setDuration(Duration.ofMinutes(40));
//        System.out.println("subtask1 START TIME___" + subtask1.getStartTime().format(dateTimeFormatter));
//        System.out.println("subtask1 Duration_____" + subtask1.getDuration().toMinutes());
//        System.out.println("subtask1 END TIME_____"+subtask1.getEndTime().format(dateTimeFormatter));
//        System.out.println("---------------------------------------------------------------------------");
//        subtask2.setStartTime(LocalDateTime.parse("2025-07-20 10:00:00", dateTimeFormatter));
//        subtask2.setDuration(Duration.ofMinutes(30));
//        System.out.println("subtask2 START TIME___"+subtask2.getStartTime().format(dateTimeFormatter));
//        System.out.println("subtask2 Duration_____" + subtask2.getDuration().toMinutes());
//        System.out.println("subtask2 END TIME_____" + subtask2.getEndTime().format(dateTimeFormatter));
//        System.out.println("---------------------------------------------------------------------------");
//        subtask3.setStartTime(LocalDateTime.parse("2025-07-25 04:00:00", dateTimeFormatter));
//        subtask3.setDuration(Duration.ofMinutes(100));
//        System.out.println("subtask2 START TIME___"+subtask3.getStartTime().format(dateTimeFormatter));
//        System.out.println("subtask2 Duration_____" + subtask3.getDuration().toMinutes());
//        System.out.println("subtask2 END TIME_____" + subtask3.getEndTime().format(dateTimeFormatter));
//
//        System.out.println("---------------------------------------------------------------------------");
//        System.out.println("epic START TIME_______"+epic.getStartTime().format(dateTimeFormatter));
//        System.out.println("epic Duration_________"+epic.getDuration().toMinutes());
//        System.out.println("epic END TIME_________"+epic.getEndTime().format(dateTimeFormatter));


        //System.out.println("endTime EPIC "+epic.getEndTime().format(dateTimeFormatter));


//        taskManager1.getTask(task1.getId());
//        taskManager1.getSubtask(subtask1.getId());
//        taskManager1.getEpic(epic.getId());

//        System.out.println("история: " + taskManager1.getHistory());

//        taskManager1.getTask(task1.getId());
//        System.out.println("история с последним таском " + taskManager1.getHistory());
//
//        taskManager1.getEpic(epic.getId());
//        System.out.println("история с последним epic " + taskManager1.getHistory());
        // ПРОВЕРЯЕМ ИСТОРИЮ ПОСЛЕ УДАЛЕНИЯ ОДНОГО ТАСКА, ЭПИКА, САБТАСКА
//        taskManager.getTask(task1.getId());
//        System.out.println("история после повторного вызова таск1: " + taskManager.getHistory());

//        taskManager.deleteEpic(epic.getId());
//        System.out.println("История после удаления эпика"+ taskManager.getHistory());

//        taskManager.deleteSubtask(subtask3.getId());
//        System.out.println("История после удаления сабтаск3 " + taskManager.getHistory());


        // ПРОВЕРЯЕМ ИСТОРИЯ ПОСЛЕ УДАЛЕНИЯ ВСЕХ ТАСКОВ, САБТАСКОВ, ЭПИКОВ
//        taskManager.clearAllTasks();
//        System.out.println("История после удаления ВСЕХ тасков "+ taskManager.getHistory());
//
//        taskManager.clearAllSubtasks();
//        System.out.println("История после удаления ВСЕХ сабтасков "+ taskManager.getHistory());
//
//        taskManager.clearAllEpics();
//        System.out.println("История после удаления ВСЕХ эпиков " + taskManager.getHistory());


    }
}
