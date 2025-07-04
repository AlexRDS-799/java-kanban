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
        } else if (type == TaskType.TASK) {
            Task task = new Task(name, description);
            task.setId(id);
            task.setStatus(status);
            return task;
        } else {
            Epic epic = new Epic(name, description);
            epic.setId(id);
            epic.setStatus(status);
            // epic.getSubtasksInThisEpic(); метод индивидуальный для эпика. После ретурна как таск, данный метод не будет работать?
            return epic;
        }
    }

    public int addTaskAfterReading(Task task) {
        if (task.getTaskType() == TaskType.TASK) {
            tasks.put(task.getId(), task);
            return task.getId();
        } else if (task.getTaskType() == TaskType.EPIC) {
            epics.put(task.getId(), (Epic) task);
            return task.getId();
        } else {
            subtasks.put(task.getId(), (Subtask) task);
            epics.get(task.getEpicId()).getSubtasksInThisEpic().add((Subtask) task); //Так же здесь, у сабтаска переданного
            //как таск, нет строки epicId. Поэтому в Task пришлось так же создать инт переменную epicID.
            return task.getId();
        }
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

            String firstHistoryLine = bufferHistory.readLine();
            while (bufferHistory.ready()) {
                String line = bufferHistory.readLine();
                historyManager.add(taskFromString(line)); //historyManager.add все принимает Task объекты, и мапа работает
                //с Тасками, так что нет смысла разделять их на эпики такски сабтаски?
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
