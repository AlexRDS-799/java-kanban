package com.yandex.app.service.In_Memory;

import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.HistoryManager;

import java.util.ArrayList;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private static int MAX_HISTORY_SIZE = 10;
    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task){
        if(history.size() < MAX_HISTORY_SIZE) {
            history.add(task);
        }else {
            history.removeFirst();
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory(){
        ArrayList<Task> clone = (ArrayList<Task>) history.clone();
        return clone;
    }

}
