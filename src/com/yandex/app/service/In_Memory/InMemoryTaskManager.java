package com.yandex.app.service.In_Memory;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.HistoryManager;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected int idTask = 0;


    protected HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }


    private boolean isTasksOverlap(Task task) {
        List<Task> listTasks = new ArrayList<>(prioritizedTasks);


        return IntStream.range(0, listTasks.size() - 1)
                .anyMatch(i -> {
                    boolean taskOverlap = false;
                    LocalDateTime currentTaskEndTime = listTasks.get(i).getEndTime();
                    LocalDateTime currentTaskStartTime = listTasks.get(i).getStartTime();
                    LocalDateTime checkTaskEndTime = task.getEndTime();

                    if (checkTaskEndTime.isAfter(currentTaskStartTime) && checkTaskEndTime.isBefore(currentTaskEndTime)) {
                        System.out.println("пересечение с - " + listTasks.get(i).getStartTime());
                        taskOverlap = true;
                    }
                    return taskOverlap;
                });
    }

    @Override
    public int addNewTask(Task task) {
        if (isTasksOverlap(task) || task.getEndTime() == null) {
            return -1;
        }
        final int id = ++idTask;
        task.setId(id);
        tasks.put(id, task);
        prioritizedTasks.add(task);
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
        if (isTasksOverlap(subtask) || subtask.getEndTime() == null) {
            return -1;
        }
        final int id = ++idTask;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).getSubtasksInThisEpic().add(subtask);
        prioritizedTasks.add(subtask);
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
        tasks.values().stream()
                .peek(task -> historyManager.remove(task))
                .peek(prioritizedTasks::remove)
                .toList();
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        Stream.concat(epics.values().stream(), subtasks.values().stream())
                .forEach(item -> {
                    historyManager.remove(item);
                    prioritizedTasks.remove(item);
                });

        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearAllSubtasks() {
        subtasks.values().stream()
                .forEach(item -> {
                    historyManager.remove(item);
                    prioritizedTasks.remove(item);
                });
        epics.values().stream()
                .forEach(epic -> epic.setStatus(Status.NEW));

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
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void updateTask(Task task) {
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);

    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        prioritizedTasks.add(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(tasks.get(id));
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        historyManager.remove(epics.get(id));
        ArrayList<Subtask> subtasksInEpic = epics.get(id).getSubtasksInThisEpic();
        for (Subtask subtask : subtasksInEpic) {
            historyManager.remove(subtask);
            prioritizedTasks.remove(subtask);
        }
        epics.get(id).getSubtasksInThisEpic().clear();
        epics.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        historyManager.remove(subtasks.get(id));
        prioritizedTasks.remove(subtasks.get(id));
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