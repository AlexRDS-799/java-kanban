package com.yandex.app.model;

public class Subtask extends Task {
    //protected int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        super.epicId = epicId;
        taskType = TaskType.SUBTASK;
    }


    @Override
    public String toString() {
        return this.description;
    }
}