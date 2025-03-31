import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        //Создали task, epic, subtask и сохранили их в HashMap
        Task task1 = TaskManager.createTask("Task1");
        Task task2 = TaskManager.createTask("Task2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = TaskManager.createEpic("Epic1");
        Subtask subtask1 = TaskManager.createSubtask("Subtask1");
        Subtask subtask2 = TaskManager.createSubtask("Subtask2");
        taskManager.addEpic(epic1);
        taskManager.addSubtask(epic1.hashCode(), subtask1);
        taskManager.addSubtask(epic1.hashCode(), subtask2);

        Epic epic2 = TaskManager.createEpic("Epic2");
        Subtask subtask3 = TaskManager.createSubtask("Subtask3");
        taskManager.addEpic(epic2);
        taskManager.addSubtask(epic2.hashCode(), subtask3);

        //Вывели статусы задач
        System.out.println(taskManager.taskStatus(task1));
        System.out.println(taskManager.epicStatus(epic1));
        System.out.println(taskManager.subtaslStatus(subtask2));

        //Вывели списки задач, а так же списки подзадач по их эпикам
        System.out.println(taskManager.tasksList());
        System.out.println(taskManager.epicsList());
        System.out.println(taskManager.subtaskList());
        System.out.println(taskManager.subtasksInTheEpic(epic1.hashCode()));
        System.out.println(taskManager.subtasksInTheEpic(epic2.hashCode()));

        //Изменили статус некоторых задач и подзадач. Проверили что они изменились
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.DONE);
        task1.setStatus(Status.IN_PROGRESS);
        System.out.println(taskManager.epicStatus(epic1));
        System.out.println(taskManager.epicStatus(epic2));
        System.out.println(taskManager.taskStatus(task1));

        //Удалили один эпик, а так же подзадачу эпика, которая изменяла его статус. Проверили вернулся ли статус в начальное значение
        taskManager.removeSubtask(epic1.hashCode(), subtask2);
        taskManager.removeEpic(epic2.hashCode());
        System.out.println(taskManager.epicsList());
        System.out.println(taskManager.subtaskList());
        System.out.println(taskManager.epicStatus(epic1));

    }
}
