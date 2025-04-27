package com.yandex.app.service.Interfaces;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.util.ArrayList;

public interface TaskManager {

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    int addNewSubtask(Subtask subtask);

    ArrayList<Task> tasksList();

    ArrayList<Epic> epicsList();

    ArrayList<Subtask> subtasksList();

    ArrayList<Task> getHistory();

    ArrayList<Subtask> subtasksInEpicList(int epicId);

    void clearAllTasks();

    void clearAllEpics();

    void clearAllSubtasks();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);


}
