package com.example.demo.task.model;

import com.example.demo.task.TaskPriority;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "TaskEntity")
@Table(name = "TASKS")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "COMPLETED", nullable = false)
    private boolean completed;

    @Basic
    private int priorityValue;

    @Transient
    private TaskPriority priority;

    @PostLoad
    void fillTransient() {
        if (priorityValue > 0) {
            this.priority = TaskPriority.of(priorityValue);
        }
    }

    @PrePersist
    void fillPersistent() {
        if (priority != null) {
            this.priorityValue = priority.getPriority();
        }
    }

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    public TaskEntity(Long id, String description, boolean completed, TaskPriority priority, LocalDateTime creationDate) {
        this.id = id;
        this.description = description;
        this.completed = completed;
        this.priority = priority;
        this.creationDate = creationDate;
    }

    public TaskEntity() {
    }

    public String toString() {
        return "TaskEntity(id=" + this.getId() + ", description=" + this.getDescription() + ", completed=" + this.isCompleted() + ", priority=" + this.getPriority() + ", creationDate=" + this.getCreationDate() + ")";
    }

    public Long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public TaskPriority getPriority() {
        return this.priority;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
