package com.yandex.app.service.In_Memory;

import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.HistoryManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task){
        if(history.size() <10) {
            history.add(task);
        }else {
            history.removeFirst();
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory(){
        return history;
    }

}
