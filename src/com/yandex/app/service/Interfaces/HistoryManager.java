package com.yandex.app.service.Interfaces;

import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public interface HistoryManager {

    void add(Task task);

    void remove(Task task);

    LinkedHashSet<Task> getHistory();
}
