import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.model.TaskType;
import com.yandex.app.service.File_Backed.FileBackedTaskManager;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;

public class Main {
    public static void main(String[] args) {
        String path = "C:\\Users\\Alexandr\\IdeaProjects\\java-kanban\\src\\com\\yandex\\app" +
                                                        "\\service\\File_Backed\\ManagerTasks\\SavedTasks.txt";
        TaskManager taskManager = Managers.getDefault();
        TaskManager taskManager1 = new FileBackedTaskManager(path);

        Task task1 = new Task("1", "first task");
        Task task2 = new Task("2", "second task");

        Epic epic = new Epic("1", "first epic");

        taskManager1.addNewEpic(epic);
        taskManager1.addNewTask(task1);
        taskManager1.addNewTask(task2);

        Subtask subtask1 = new Subtask("1", "first subtask", epic.getId());
        Subtask subtask2 = new Subtask("2", "second subtask", epic.getId());
        Subtask subtask3 = new Subtask("3", "third subtask", epic.getId());

        taskManager1.addNewSubtask(subtask1);
        taskManager1.addNewSubtask(subtask2);
        taskManager1.addNewSubtask(subtask3);

        taskManager1.getTask(task1.getId());
        taskManager1.getSubtask(subtask1.getId());
        taskManager1.getEpic(epic.getId());

        System.out.println("история: " + taskManager1.getHistory());

        taskManager1.getTask(task1.getId());
        System.out.println("история с последним таском " + taskManager1.getHistory());

        taskManager1.getEpic(epic.getId());
        System.out.println("история с последним epic " + taskManager1.getHistory());
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
