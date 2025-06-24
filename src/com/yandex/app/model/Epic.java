package com.yandex.app.model;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Subtask> subtasksInThisEpic = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Subtask> getSubtasksInThisEpic() {
        return subtasksInThisEpic;

    }

    @Override
    public String toString() {
        return this.description;
    }
}
