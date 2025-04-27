package com.yandex.app.service.Interfaces;

import com.yandex.app.model.Task;

import java.util.ArrayList;

public interface HistoryManager {

    void add(Task task);

    ArrayList<Task> getHistory();
}
