package com.yandex.app;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.In_Memory.InMemoryHistoryManager;
import com.yandex.app.service.In_Memory.InMemoryTaskManager;
import com.yandex.app.service.Interfaces.HistoryManager;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;

public class Main {
    public static void main(String[] args) {
        //Привет) После разъяснения понял суть задачи и решил переписать весь код заного. Теперь почти всё понятно
        //но остается загадкой, какая реализация методов updateTask,Epic,Subtask?
        Managers managers = new Managers();
        TaskManager inMemoryTaskManager = managers.getDefault();

        Task task1 = new Task("Task1", "Descript1");
        Task task2 = new Task("Task2", "Descript2");
        Task task3 = new Task("Task3", "Descript3");
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewTask(task2);
        inMemoryTaskManager.addNewTask(task3);

        Epic epic1 = new Epic("Epic1", "DescriptEpic1");
        Epic epic2 = new Epic("Epic2", "DescriptEpic2");
        inMemoryTaskManager.addNewEpic(epic1);
        inMemoryTaskManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask1", "DescriptSubtask1", epic1.getId());
        Subtask subtask2 = new Subtask("Subtask2", "DescriptSubtask2", epic1.getId());
        Subtask subtask3 = new Subtask("Subtask3", "DescriptSubtask3", epic1.getId());
        Subtask subtask4 = new Subtask("Subtask4", "DescriptSubtask4", epic2.getId());
        inMemoryTaskManager.addNewSubtask(subtask2);
        inMemoryTaskManager.addNewSubtask(subtask3);
        inMemoryTaskManager.addNewSubtask(subtask4);

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getEpic(epic2.getId());
        inMemoryTaskManager.getSubtask(subtask3.getId());
        inMemoryTaskManager.getSubtask(subtask3.getId());
        inMemoryTaskManager.getSubtask(subtask3.getId());
        inMemoryTaskManager.getSubtask(subtask3.getId());
        inMemoryTaskManager.getSubtask(subtask3.getId());
        inMemoryTaskManager.getSubtask(subtask3.getId());
        inMemoryTaskManager.getEpic(epic2.getId());

        System.out.println(inMemoryTaskManager.getHistory());


    }
}
