package com.yandex.app.model;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(String name, String description, int epicId){
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId(){
        return epicId;

    }

    @Override
    public String toString() {
        return this.description;
    }
}