package com.yandex.app.service;

import com.yandex.app.service.File_Backed.FileBackedTaskManager;
import com.yandex.app.service.In_Memory.InMemoryHistoryManager;
import com.yandex.app.service.In_Memory.InMemoryTaskManager;
import com.yandex.app.service.Interfaces.HistoryManager;
import com.yandex.app.service.Interfaces.TaskManager;

public class Managers {

    private Managers() {

    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getFileBackedManager(String pathTask, String pathHistory) {
        return new FileBackedTaskManager(pathTask, pathHistory);
    }

}
