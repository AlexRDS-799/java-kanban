package com.yandex.app.tests;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static Managers manager = new Managers();
    static TaskManager taskManager = manager.getDefault();
    Task task1;
    Epic epic1;

    @BeforeEach
    void beforeEach(){
        task1 = new Task("Name", "Description");
        taskManager.addNewTask(task1);
        epic1 = new Epic("Name", "Description");
        taskManager.addNewEpic(epic1);
        taskManager.getHistory().clear();
    }

    @Test
    void historyEmpty(){
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void historyThreeTasks(){
        taskManager.getEpic(epic1.getId());
        getTaskNumberTimes(task1, 2);

        assertEquals(3, taskManager.getHistory().size());
        assertEquals(epic1, taskManager.getHistory().getFirst()); //предыдущая версия epic1 равна epic хранящемуся в истории
    }

    @Test
    void historyElevenTasks(){
        taskManager.getEpic(epic1.getId());
        getTaskNumberTimes(task1, 10);

        assertEquals(10, taskManager.getHistory().size());
        assertEquals(task1, taskManager.getHistory().getFirst()); //первым элементом был добавлен эпик. После добавления 11-го
        // элемента ожидаем первым элементов в истории - task1
    }

    public void getTaskNumberTimes(Task task, int times){
        for (int i = 1; i <= times; i++) {
            taskManager.getTask(task.getId());
        }
    }

}