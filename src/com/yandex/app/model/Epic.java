package com.yandex.app.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class Epic extends Task {
    protected ArrayList<Subtask> subtasksInThisEpic = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
        taskType = TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        Optional<LocalDateTime> endTimeOpt = subtasksInThisEpic.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo);
        return endTimeOpt.orElse(LocalDateTime.MIN);
    }

    @Override
    public Duration getDuration(){
        this.duration = subtasksInThisEpic.stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
        return duration;
    }

    @Override
    public LocalDateTime getStartTime(){
        Optional<LocalDateTime> startTimeOpt = subtasksInThisEpic.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);
        this.startTime = startTimeOpt.orElse(LocalDateTime.MIN);
        return startTime;
    }

    public ArrayList<Subtask> getSubtasksInThisEpic() {
        return subtasksInThisEpic;

    }

    @Override
    public String toString() {
        return this.description;
    }
}
