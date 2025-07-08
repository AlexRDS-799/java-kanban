package com.yandex.app.service.File_Backed;

import com.yandex.app.model.*;
import com.yandex.app.service.In_Memory.InMemoryTaskManager;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.TaskManagerExceptions.ManagerSaveException;

import java.io.*;
import java.util.ArrayList;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final String pathTasks;
    private final String pathHistory;


    public FileBackedTaskManager(String pathFileTasks, String pathFileHistory) {
        this.pathTasks = pathFileTasks;
        this.pathHistory = pathFileHistory;
        loadFromFile(new File(pathFileTasks), new File(pathFileHistory));
    }

    void save() {

        try (BufferedWriter bufferTasks = new BufferedWriter(new FileWriter(pathTasks));
             BufferedWriter bufferHistory = new BufferedWriter(new FileWriter(pathHistory))) {

            bufferTasks.write("id,type,name,status,description,epic");
            bufferHistory.write("id,type,name,status,description,epic");
            bufferTasks.flush();
            bufferHistory.flush();

            for (Task task : tasksList()) {
                bufferTasks.write("\n" + taskInString(task));
            }
            for (Epic epic : epicsList()) {
                bufferTasks.write("\n" + taskInString(epic));
            }
            for (Subtask subtask : subtasksList()) {
                bufferTasks.write("\n" + taskInString(subtask) + "," + subtask.getEpicId());
            }
            for (Task task : getHistory()) {
                String taskString = taskInString(task);
                if (task.getTaskType() == TaskType.SUBTASK) {
                    Subtask sub = (Subtask) task;
                    taskString = taskInString(task) + "," + sub.getEpicId();
                }
                bufferHistory.write("\n" + taskString);
            }
        } catch (FileNotFoundException fileNotFound) {
            throw new ManagerSaveException("Файла по указанному пути не существует!");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении менеджера в файл!");
        }

    }

    public Task taskFromString(String line) {

        String[] tasks = line.split(",");
        final int id = Integer.parseInt(tasks[0]);
        final TaskType type = TaskType.valueOf(tasks[1]);
        final String name = tasks[2];
        final Status status = Status.valueOf(tasks[3]);
        final String description = tasks[4];
        final int epicId;

        if (type == TaskType.SUBTASK) {
            epicId = Integer.parseInt(tasks[5]);
            Subtask subtask = new Subtask(name, description, epicId);
            subtask.setId(id);
            subtask.setStatus(status);
            return subtask;
        }

        if (type == TaskType.TASK) {
            Task task = new Task(name, description);
            task.setId(id);
            task.setStatus(status);
            return task;
        }

        Epic epic = new Epic(name, description);
        epic.setId(id);
        epic.setStatus(status);


        return epic;
    }

    public int addTaskAfterReading(Task task) {

        switch (task.getTaskType()) {
            case TASK:
                tasks.put(task.getId(), task);
                break;
            case EPIC:
                epics.put(task.getId(), (Epic) task);
                break;
            case SUBTASK:
                subtasks.put(task.getId(), (Subtask) task);
                break;
        }
        return task.getId();
    }

    public void loadFromFile(File fileTask, File fileHistory) {

        try (BufferedReader bufferTasks = new BufferedReader(new FileReader(fileTask));
             BufferedReader bufferHistory = new BufferedReader(new FileReader(fileHistory))) {

            String firstLine = bufferTasks.readLine();
            while (bufferTasks.ready()) {
                String line = bufferTasks.readLine();
                int taskId = addTaskAfterReading(taskFromString(line));
                if (idTask < taskId) {
                    idTask = taskId;
                }
            }

            for (Subtask subtask : subtasks.values()) {
                epics.get(subtask.getEpicId()).getSubtasksInThisEpic().add(subtask);
            }

            String firstHistoryLine = bufferHistory.readLine();
            while (bufferHistory.ready()) {
                String line = bufferHistory.readLine();
                historyManager.add(taskFromString(line));
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл по указанному пути не существует!");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при обработки данных файла!");
        }

    }

    public String taskInString(Task task) {
        return task.getId() + "," + task.getTaskType().toString() + ","
                + task.getName() + "," + task.getStatus().toString() + "," + task.getDescription();
    }

    @Override
    public int addNewEpic(Epic epic) {
        int epicId = super.addNewEpic(epic);
        save();
        return epicId;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        int subtaskId = super.addNewSubtask(subtask);
        save();
        return subtaskId;
    }

    @Override
    public int addNewTask(Task task) {
        int taskId = super.addNewTask(task);
        save();
        return taskId;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        save();
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtasks.get(id));
        save();
        return subtasks.get(id);
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        save();
        return tasks.get(id);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return super.getHistory();
    }
}
