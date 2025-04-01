package com.yandex.app.service;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> Tasks = new HashMap<>();
    private final HashMap<Integer, Epic> Epics = new HashMap<>();
    private final HashMap<Integer, Subtask> Subtasks = new HashMap<>();
    public static int idTask = 0;

    public int addNewTask(Task task){
        Tasks.put(task.getId(), task);
        return task.getId();
    }

    public int addNewEpic(Epic epic){
        Epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public int addNewSubtask(Subtask subtask){
        Subtasks.put(subtask.getId(), subtask);
        Epics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
        return subtask.getId();
    }

    public ArrayList<String> tasksList(){
        ArrayList<String> tasks = new ArrayList<>();
        for (Task task: Tasks.values()){
            tasks.add(task.getTask());
        }
        return tasks;
    }

    public ArrayList<String> epicsList(){
        ArrayList<String> epics = new ArrayList<>();
        for (Epic epic: Epics.values()){
            epics.add(epic.getTask());
        }
        return epics;
    }

    public ArrayList<String> subtasksList(){
        ArrayList<String> subtasks = new ArrayList<>();
        for(Subtask subtask: Subtasks.values()){
            subtasks.add(subtask.getTask());
        }
        return subtasks;
    }

    public ArrayList<String> subtasksInEpicList(int epicId){
        ArrayList<Integer> subtasksId = Epics.get(epicId).getSubtaskIds();
        ArrayList<String> subtasks = new ArrayList<>();
        for (int id: subtasksId){
            subtasks.add(Subtasks.get(id).getTask());
        }
        return subtasks;
    }

    public void clearAllTasks(){
        Tasks.clear();
    }

    public void clearAllEpics(){
        Epics.clear();
        Subtasks.clear();
    }

    public void clearAllSubtasks(){
        for(Epic epic: Epics.values()){
            epic.getSubtaskIds().clear();
            epic.setStatus(Status.NEW);
        }
        Subtasks.clear();
    }

    public Task getTask(int id){
        return Tasks.get(id);
    }

    public Epic getEpic(int id){
        return Epics.get(id);
    }

    public Subtask getSubtask(int id){
        return Subtasks.get(id);
    }

    public void updateTask(Task task){
        Tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic){
        Epics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask){
        Subtasks.put(subtask.getId(), subtask);
        Epics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
        updateEpicStatus(subtask.getEpicId());
    }

    public void deleteTask(int id){
        Tasks.remove(id);
    }

    public void deleteEpic(int id){
        ArrayList<Integer> subtaskIds = Epics.get(id).getSubtaskIds();
        for (int subtaskId: subtaskIds){
            Subtasks.remove(subtaskId);
        }
        Epics.remove(id);
    }

    public void deleteSubtask(int id){
        Epics.get(Subtasks.get(id).getEpicId()).getSubtaskIds().remove(id);
        Subtasks.remove(id);
    }

    public void updateEpicStatus(int epicId){
        ArrayList<Integer> subtaskIds = Epics.get(epicId).getSubtaskIds();
        if(subtaskIds.isEmpty()){
            Epics.get(epicId).setStatus(Status.NEW);
        }
        boolean allNew = true;
        boolean allDone = true;
        for(int id: subtaskIds) {
            if (Subtasks.get(id).getStatus() != Status.NEW) {
                allNew = false;
            }
            if (Subtasks.get(id).getStatus() != Status.DONE) {
                allDone = false;
            }
        }
        if(allNew){
            Epics.get(epicId).setStatus(Status.NEW);
        } else if (allDone) {
            Epics.get(epicId).setStatus(Status.DONE);
        }else {
            Epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }
}