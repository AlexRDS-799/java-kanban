package com.yandex.app.service.File_Backed;

import com.yandex.app.model.*;
import com.yandex.app.service.In_Memory.InMemoryTaskManager;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.TaskManagerExceptions.ManagerSaveException;

import java.io.*;


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
                bufferTasks.write("\n" + taskInString(subtask));
            }
            for (Task task : getHistory()) {
                bufferHistory.write("\n" + taskInString(task));
            }
        } catch (FileNotFoundException fileNotFound) {
            throw new ManagerSaveException("Файла по указанному пути не существует!");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении менеджера в файл!");
        }

    }

    public void loadFromFile(File fileTask, File fileHistory) {

        try (BufferedReader bufferTasks = new BufferedReader(new FileReader(fileTask));
             BufferedReader bufferHistory = new BufferedReader(new FileReader(fileHistory))) {

            int savedTaskId = 0;
            while (bufferTasks.ready()) {
                String line = bufferTasks.readLine();
                String[] tasksInLine = line.split(",");

                if (tasksInLine[1].equals(TaskType.TASK.toString())) {
                    int id = Integer.parseInt(tasksInLine[0]);
                    if (savedTaskId < id) {
                        savedTaskId = id;
                    }
                    Task task = new Task(tasksInLine[2], tasksInLine[4]);
                    task.setId(id);
                    task.setStatus(Status.valueOf(tasksInLine[3]));
                    tasks.put(id, task);
                } else if (tasksInLine[1].equals(TaskType.EPIC.toString())) {
                    int id = Integer.parseInt(tasksInLine[0]);
                    if (savedTaskId < id) {
                        savedTaskId = id;
                    }
                    Epic epic = new Epic(tasksInLine[2], tasksInLine[4]);
                    epic.setId(id);
                    epic.setStatus(Status.valueOf(tasksInLine[3]));
                    epics.put(id, epic);
                } else if (tasksInLine[1].equals(TaskType.SUBTASK.toString())) {
                    int id = Integer.parseInt(tasksInLine[0]);
                    if (savedTaskId < id) {
                        savedTaskId = id;
                    }
                    int epicId = Integer.parseInt(tasksInLine[5]);
                    Subtask subtask = new Subtask(tasksInLine[2], tasksInLine[4], epicId);
                    subtask.setId(id);
                    subtasks.put(id, subtask);
                }
            }
            idTask = savedTaskId;

            while (bufferHistory.ready()) {
                String line = bufferHistory.readLine();
                String[] tasksInLine = line.split(",");

                if (tasksInLine[1].equals(TaskType.TASK.toString())) {
                    historyManager.add(tasks.get(Integer.parseInt(tasksInLine[0])));
                } else if (tasksInLine[1].equals(TaskType.EPIC.toString())) {
                    historyManager.add(epics.get(Integer.parseInt(tasksInLine[0])));
                } else if (tasksInLine[1].equals(TaskType.SUBTASK.toString())) {
                    historyManager.add(subtasks.get(Integer.parseInt(tasksInLine[0])));
                }
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл по указанному пути не существует!");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при обработки данных файла!");
        }


    }

    public String taskInString(Task task) {
        String infoTask = task.getId() + "," + task.getTaskType().toString() + ","
                + task.getName() + "," + task.getStatus().toString() + "," + task.getDescription();
        if (task.getTaskType().toString().equals("SUBTASK")) {
            return infoTask + "," + task.getEpicId();
        } else return infoTask;
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
        save();
        return super.getEpic(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        save();
        return super.getSubtask(id);
    }

    @Override
    public Task getTask(int id) {
        save();
        return super.getTask(id);
    }
}
