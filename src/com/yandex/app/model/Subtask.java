package com.yandex.app.model;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(String task, String description, int epicId){
        super(task, description);
        this.epicId = epicId;
    }

    public int getEpicId(){
        return epicId;

    }
}

