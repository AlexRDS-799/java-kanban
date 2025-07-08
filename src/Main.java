
import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;


public class Main {
    public static void main(String[] args) {

        String pathTasks = "C:\\Users\\Alexandr\\IdeaProjects\\java-kanban\\src\\com\\yandex\\app" +
                "\\service\\File_Backed\\SavedManager\\SavedTasks.txt";
        String pathHistory = "C:\\Users\\Alexandr\\IdeaProjects\\java-kanban\\src\\com\\yandex\\app" +
                "\\service\\File_Backed\\SavedManager\\SavedHistory.txt";

        TaskManager fileBackedTaskManager = Managers.getFileBackedManager(pathTasks, pathHistory);


        Task task1 = new Task("Task1", "first task");
        Task task2 = new Task("Task2", "second task");

        Epic epic = new Epic("Epic1", "first epic");

        fileBackedTaskManager.addNewEpic(epic);
        fileBackedTaskManager.addNewTask(task1);
        fileBackedTaskManager.addNewTask(task2);

        fileBackedTaskManager.getTask(task1.getId());
        fileBackedTaskManager.getEpic(epic.getId());
        fileBackedTaskManager.getEpic(epic.getId());
        Subtask subtask1 = new Subtask("Subtask1", "first subtask", epic.getId());
        Subtask subtask2 = new Subtask("Subtask2", "second subtask", epic.getId());
        Subtask subtask3 = new Subtask("Subtask3", "third subtask", epic.getId());

        fileBackedTaskManager.addNewSubtask(subtask1);
        fileBackedTaskManager.addNewSubtask(subtask2);
        fileBackedTaskManager.addNewSubtask(subtask3);

        System.out.println("tasks: " + fileBackedTaskManager.tasksList());
        System.out.println("epics: " + fileBackedTaskManager.epicsList());
        System.out.println("subtasks: " + fileBackedTaskManager.subtasksList());
        System.out.println("history: " + fileBackedTaskManager.getHistory());
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
