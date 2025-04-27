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

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static int idTask = 0;

    Managers managers = new Managers();
    HistoryManager inMemoryHistoryManager = managers.getDefaultHistory();

    @Override
    public void addNewTask(Task task){
        final int id = ++idTask;
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void addNewEpic(Epic epic){
        final int id = ++idTask;
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void addNewSubtask(Subtask subtask){
        final int id = ++idTask;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).getSubtasksInThisEpic().add(subtask);
    }

    @Override
    public ArrayList<Task> tasksList(){
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public ArrayList<Epic> epicsList(){
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public ArrayList<Subtask> subtasksList(){
        return new ArrayList<>(this.subtasks.values());
    }

    @Override
    public ArrayList<Subtask> subtasksInEpicList(int epicId){
        return epics.get(epicId).getSubtasksInThisEpic();
    }

    @Override
    public void clearAllTasks(){
        tasks.clear();
    }

    @Override
    public void clearAllEpics(){
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearAllSubtasks(){
        for(Epic epic: epics.values()){
            epic.getSubtasksInThisEpic().clear();
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
    }

    @Override
    public Task getTask(int id){
        inMemoryHistoryManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id){
        inMemoryHistoryManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id){
       inMemoryHistoryManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public ArrayList<Task> getHistory(){
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void deleteTask(int id){
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id){
        epics.get(id).getSubtasksInThisEpic().clear();
        epics.remove(id);
    }

    @Override
    public void deleteSubtask(int id){
        epics.get(subtasks.get(id).getEpicId()).getSubtasksInThisEpic().remove(subtasks.get(id));
        subtasks.remove(id);
    }

    @Override
    public void updateEpicStatus(int epicId){
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