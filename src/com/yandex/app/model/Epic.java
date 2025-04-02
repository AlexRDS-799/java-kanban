package com.yandex.app.model;

import java.util.ArrayList;

public class Epic extends Task{
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String task, String description){
        super(task, description);
    }

    public ArrayList<Integer> getSubtaskIds(){
        return subtaskIds;

    }

}
