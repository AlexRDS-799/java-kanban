package test;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    //в этом же тесте проверена инициализация объектов через класс Managers. Создается дефолтный менеджер с обнуленным idTasks
    private static TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addNewTask() {
        Task task = new Task("Name", "Description");
        final int taskId = taskManager.addNewTask(task);

        final Task taskTest = taskManager.getTask(taskId);

        assertNotNull(taskManager.tasksList());
        assertEquals(task, taskTest);
        assertEquals(1, taskManager.tasksList().size());
        assertEquals(1, taskManager.getTask(taskId).getId());
        assertEquals(task, taskManager.getTask(taskId));
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Name", "Description");

        final int epicId = taskManager.addNewEpic(epic);

        final Epic epicTest = taskManager.getEpic(epicId);
        assertNotNull(taskManager.epicsList());
        assertEquals(epic, epicTest);
        assertEquals(1, taskManager.epicsList().size());
        assertEquals(1, taskManager.getEpic(epicId).getId());
        assertEquals(epic, taskManager.getEpic(epicId));

        //taskManager.addNewSubtask(epic); эпик невозможно добавить в список сабтасков, т.к. метод принимает объект эпика
        // а эпик объект приравнять к сабтаску не возможно, т.к. разные параметры методов (создание сабтаска требует epicId
    }

    @Test
    void addNewSubtask() {
        Epic epic = new Epic("Name", "Description");
        taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Name", "Description", epic.getId());
        final int subtaskId = taskManager.addNewSubtask(subtask);

        Subtask subtaskTest = taskManager.getSubtask(subtaskId);
        assertNotNull(taskManager.subtasksList());
        assertEquals(subtask, subtaskTest);
        assertEquals(1, taskManager.subtasksList().size());
        assertEquals(2, taskManager.getSubtask(subtaskId).getId());
        assertEquals(subtask, taskManager.getSubtask(subtaskId));
    }

    @Test
    void addsDifferentTypesOfTasks() {
        Epic epic = new Epic("Name", "Description");
        taskManager.addNewEpic(epic);
        Task task = new Task("Name", "Description");
        taskManager.addNewTask(task);
        Subtask subtask = new Subtask("Name", "Description", epic.getId());
        taskManager.addNewSubtask(subtask);

        assertEquals(epic, taskManager.getEpic(1));
        assertEquals(task, taskManager.getTask(2));
        assertEquals(subtask, taskManager.getSubtask(3));

    }
}