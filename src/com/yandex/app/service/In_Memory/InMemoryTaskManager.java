package com.yandex.app.service.In_Memory;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.HistoryManager;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int idTask = 0;


    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public int addNewTask(Task task) {
        final int id = ++idTask;
        task.setId(id);
        tasks.put(id, task);
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = ++idTask;
        epic.setId(id);
        epics.put(id, epic);
        return epic.getId();
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        final int id = ++idTask;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).getSubtasksInThisEpic().add(subtask);
        return subtask.getId();
    }

    @Override
    public ArrayList<Task> tasksList() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public ArrayList<Epic> epicsList() {
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public ArrayList<Subtask> subtasksList() {
        return new ArrayList<>(this.subtasks.values());
    }

    @Override
    public ArrayList<Subtask> subtasksInEpicList(int epicId) {
        return epics.get(epicId).getSubtasksInThisEpic();
    }

    @Override
    public void clearAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic);
        }

        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask);
        }

        for (Epic epic : epics.values()) {
            epic.getSubtasksInThisEpic().clear();
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public LinkedHashSet<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        historyManager.remove(epics.get(id));
        ArrayList<Subtask> subtasksInEpic = epics.get(id).getSubtasksInThisEpic();
        for (Subtask subtask : subtasksInEpic) {
            historyManager.remove(subtask);
        }
        epics.get(id).getSubtasksInThisEpic().clear();
        epics.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        historyManager.remove(subtasks.get(id));
        epics.get(subtasks.get(id).getEpicId()).getSubtasksInThisEpic().remove(subtasks.get(id));
        subtasks.remove(id);
    }


    private void updateEpicStatus(int epicId) {
        ArrayList<Subtask> subtaskInThisEpic = epics.get(epicId).getSubtasksInThisEpic();
        if (subtaskInThisEpic.isEmpty()) {
            epics.get(epicId).setStatus(Status.NEW);
        }
        boolean allNew = true;
        boolean allDone = true;
        for (Subtask subtask : subtaskInThisEpic) {
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }
        if (allNew) {
            epics.get(epicId).setStatus(Status.NEW);
        } else if (allDone) {
            epics.get(epicId).setStatus(Status.DONE);
        } else {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }

}