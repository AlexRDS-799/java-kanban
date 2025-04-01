package com.yandex.app;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.TaskManager;

public class Main {
    public static void main(String[] args) {
        //Привет) После разъяснения понял суть задачи и решил переписать весь код заного. Теперь почти всё понятно
        //но остается загадкой, какая реализация методов updateTask,Epic,Subtask?
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Task1", "Descript1");
        Task task2 = new Task("Task2", "Descript2");
        Task task3 = new Task("Task3", "Descript3");
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);

        Epic epic1 = new Epic("Epic1", "DescriptEpic1");
        Epic epic2 = new Epic("Epic2", "DescriptEpic2");
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask1", "DescriptSubtask1", epic1.getId());
        Subtask subtask2 = new Subtask("Subtask2", "DescriptSubtask2", epic1.getId());
        Subtask subtask3 = new Subtask("Subtask3", "DescriptSubtask3", epic1.getId());
        Subtask subtask4 = new Subtask("Subtask4", "DescriptSubtask4", epic2.getId());
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);
        taskManager.addNewSubtask(subtask4);

        System.out.println("Получение списка всех задач");
        System.out.println(taskManager.tasksList());
        System.out.println(taskManager.epicsList());
        System.out.println(taskManager.subtasksList());

        System.out.println("Получение по идентефикатору");
        System.out.println(taskManager.getTask(task1.getId()).getTask());
        System.out.println(taskManager.getEpic(epic2.getId()).getTask());
        System.out.println(taskManager.getSubtask(subtask3.getId()).getTask());

        System.out.println("Получение подзадач по эпику");
        System.out.println(taskManager.subtasksInEpicList(epic1.getId()));

        System.out.println("Получение статуса таска");
        task2.setStatus(Status.DONE);
        System.out.println("Task1 status - " + task1.getStatus());
        System.out.println("Task2 status - " + task2.getStatus());

        System.out.println("Получение статуса эпика");
        System.out.println("Epic status before - " + epic1.getStatus());
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.DONE);
        taskManager.updateEpicStatus(epic1.getId());
        System.out.println("Epic status after setStatus - " + epic1.getStatus());
        taskManager.clearAllSubtasks();
        System.out.println("Epic status after clearAllSubtasks - " + epic1.getStatus());

    }
}
