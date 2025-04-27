package com.yandex.app.service;

import com.yandex.app.service.In_Memory.InMemoryHistoryManager;
import com.yandex.app.service.In_Memory.InMemoryTaskManager;
import com.yandex.app.service.Interfaces.HistoryManager;
import com.yandex.app.service.Interfaces.TaskManager;

public class Managers {

    public TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

}
