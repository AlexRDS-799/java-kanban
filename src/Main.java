import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.addEpic(TaskManager.createEpic("Epic1"));
        taskManager.addSubtask("Epic1".hashCode(),TaskManager.createSubtask("Подзадача1"));
        taskManager.addSubtask("Epic1".hashCode(),TaskManager.createSubtask("Подзадача2"));

        System.out.println(taskManager.subtasksInTheEpic("Epic1".hashCode()));

    }
}
