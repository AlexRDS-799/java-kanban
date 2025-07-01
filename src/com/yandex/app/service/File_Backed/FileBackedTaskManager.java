package com.yandex.app.service.File_Backed;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.In_Memory.InMemoryTaskManager;
import com.yandex.app.service.Interfaces.TaskManager;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private String path;

    public FileBackedTaskManager(String pathFile) {
        this.path = pathFile;

        try (BufferedWriter bufw = new BufferedWriter(new FileWriter(path, true))) {
            bufw.write("id,type,name,status,description,epic");
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Файла по указанному пути не существует!");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении менеджера в файл!");
            return;
        }

    }

    void save(Task task) {

        try (BufferedWriter bufw = new BufferedWriter(new FileWriter(path, true))) {

            bufw.write("\n" + taskInString(task));

        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Файла по указанному пути не существует!");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении менеджера в файл!");
            return;
        }

    }

    public String taskInString(Task task){
        String infoTask =  Integer.toString(task.getId()) + "," + task.getTaskType().toString() + ","
                + task.getName() + "," + task.getStatus().toString() + "," + task.getDescription();
        if (task.getTaskType().toString().equals("SUBTASK")){
            return infoTask + "," + task.getEpicId();
        }else return infoTask;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int epicId = super.addNewEpic(epic);
        save(epic);
        return epicId;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        int subtaskId = super.addNewSubtask(subtask);
        save(subtask);
        return subtaskId;
    }

    @Override
    public int addNewTask(Task task) {
        int taskId = super.addNewTask(task);
        save(task);
        return taskId;
    }

}
