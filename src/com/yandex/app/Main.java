package com.yandex.app;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.In_Memory.InMemoryHistoryManager;
import com.yandex.app.service.In_Memory.InMemoryTaskManager;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("1", "first task");
        Task task2 = new Task("2", "second task");

        Epic epic = new Epic("1", "first epic");


        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("1", "first subtask", epic.getId());
        Subtask subtask2 = new Subtask("2", "second subtask", epic.getId());
        Subtask subtask3 = new Subtask("3", "third subtask", epic.getId());

        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);

        System.out.println(taskManager.tasksList());
        System.out.println(taskManager.epicsList());
        System.out.println(taskManager.subtasksList());

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());

        taskManager.getEpic(epic.getId());

        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getSubtask(subtask3.getId());

        System.out.println("история: "+taskManager.getHistory());
        taskManager.getTask(task1.getId());
        System.out.println("история после повторного вызова таск1: "+ taskManager.getHistory());


    }
}
