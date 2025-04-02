package com.yandex.app.service;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static int idTask = 0;


    public int addNewTask(Task task){
        final int id = ++idTask;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public int addNewEpic(Epic epic){
        final int id = ++idTask;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    public int addNewSubtask(Subtask subtask){
        final int id = ++idTask;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).getSubtasksInThisEpic().add(subtask);
        return id;
    }

    public ArrayList<Task> tasksList(){
        return new ArrayList<>(this.tasks.values()); //То есть лучше возвращать список объектовв, а не строк?
    }

    public ArrayList<Epic> epicsList(){
        return new ArrayList<>(this.epics.values());
    }

    public ArrayList<Subtask> subtasksList(){
        return new ArrayList<>(this.subtasks.values());
    }

    public ArrayList<Subtask> subtasksInEpicList(int epicId){
        return epics.get(epicId).getSubtasksInThisEpic();
    }

    public void clearAllTasks(){
        tasks.clear();
    }

    public void clearAllEpics(){
        epics.clear();
        subtasks.clear();
    }

    public void clearAllSubtasks(){
        for(Epic epic: epics.values()){
            epic.getSubtasksInThisEpic().clear();
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
    }

    public Task getTask(int id){
        return tasks.get(id);
    }

    public Epic getEpic(int id){
        return epics.get(id);
    }

    public Subtask getSubtask(int id){
        return subtasks.get(id);
    }

    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    public void updateSubtask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public void deleteTask(int id){
        tasks.remove(id);
    }

    public void deleteEpic(int id){
        epics.get(id).getSubtasksInThisEpic().clear();
        epics.remove(id);
    }

    public void deleteSubtask(int id){
        epics.get(subtasks.get(id).getEpicId()).getSubtasksInThisEpic().remove(subtasks.get(id));
        subtasks.remove(id);
    }

    private void updateEpicStatus(int epicId){
        ArrayList<Subtask> subtaskInThisEpic = epics.get(epicId).getSubtasksInThisEpic();
        if(subtaskInThisEpic.isEmpty()){
            epics.get(epicId).setStatus(Status.NEW);
        }
        boolean allNew = true;
        boolean allDone = true;
        for(Subtask subtask: subtaskInThisEpic) {
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }
        if(allNew){
            epics.get(epicId).setStatus(Status.NEW);
        } else if (allDone) {
            epics.get(epicId).setStatus(Status.DONE);
        }else {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }
}