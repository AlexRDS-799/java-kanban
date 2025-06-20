package com.yandex.app.tests;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static TaskManager taskManager;
    Task task1;
    Epic epic1;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        task1 = new Task("Name", "task1");
        taskManager.addNewTask(task1);
        epic1 = new Epic("Name", "epic1");
        taskManager.addNewEpic(epic1);
        subtask1 = new Subtask("Name", "subtask1", epic1.getId());
        subtask2 = new Subtask("Name", "subtask2", epic1.getId());
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);

        taskManager.getEpic(epic1.getId());
        getTaskNumberTimes(task1, 7);
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        getTaskNumberTimes(task1, 1);

        System.out.println(taskManager.getHistory());
    }

    @Test
    void historyEmpty() { //после удаления всех задач, история должна быть пустой
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();
        taskManager.clearAllSubtasks();
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void repeatedTasksInHistory() { //Проверяем что после добавления 8 одинаковых тасков, в историю будет добавлен только 1
        assertEquals(4, taskManager.getHistory().size());
        assertEquals(epic1, taskManager.getHistory().getFirst()); //предыдущая версия epic1 равна epic хранящемуся в истории
        assertEquals(task1, taskManager.getHistory().getLast()); //сохранится только таск, вызваный последним
    }

    @Test
    void removeTaskFromHistory() {
        taskManager.deleteTask(task1.getId()); //после удаления таска, из истории удалится 1 объект
        assertEquals(3, taskManager.getHistory().size());
    }

    @Test
    void removeEpicFromHistory() {
        taskManager.deleteEpic(epic1.getId());//после удаления epic удалятся и сабтаски этого эпика(3 объекта)
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void removeSubtaskFromHistory() {
        taskManager.deleteSubtask(subtask1.getId());//удален 1 объект
        assertEquals(3, taskManager.getHistory().size());
    }

    public void getTaskNumberTimes(Task task, int times) {
        for (int i = 1; i <= times; i++) {
            taskManager.getTask(task.getId());
        }
    }

}