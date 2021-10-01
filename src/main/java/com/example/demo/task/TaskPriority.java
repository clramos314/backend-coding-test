package com.example.demo.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskPriority {
    LOW(100), MEDIUM(200), HIGH(300);

    private int priority;

    TaskPriority(int priority) {
        this.priority = priority;
    }

    @JsonValue
    public int getPriority() {
        return priority;
    }

    public static TaskPriority of(int priority) {
        return Stream.of(TaskPriority.values())
                .filter(p -> p.getPriority() == priority)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
