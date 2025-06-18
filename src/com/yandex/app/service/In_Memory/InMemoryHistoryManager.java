package com.yandex.app.service.In_Memory;

import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.HistoryManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {

    private LinkedHashSet<Task> history = new LinkedHashSet<>();

    @Override
    public void add(Task task) {
        if (history.contains(task)) {
            history.remove(task);
            history.add(task);
        } else {
            history.add(task);
        }
    }

    @Override
    public void remove(Task task) {
        history.remove(task);
    }


    @Override
    public LinkedHashSet<Task> getHistory() {

        LinkedHashSet<Task> clone = (LinkedHashSet<Task>) history.clone();
        return clone;
    }


}
