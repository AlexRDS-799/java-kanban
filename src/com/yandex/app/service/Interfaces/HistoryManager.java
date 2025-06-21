package com.yandex.app.service.Interfaces;

import com.yandex.app.model.Task;
import com.yandex.app.service.In_Memory.Node;


import java.util.ArrayList;
import java.util.LinkedHashSet;

public interface HistoryManager {

    void add(Task task);

    void remove(Task task);

    ArrayList<Task> getHistory();
}
