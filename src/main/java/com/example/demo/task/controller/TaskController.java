package com.example.demo.task.controller;

import java.time.LocalDateTime;
import java.util.List;
import com.example.demo.task.error.TaskNotFoundException;
import com.example.demo.task.model.TaskEntity;
import com.example.demo.task.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    /**
     * Get all tasks
     *
     * @return 404 with no tasks, otherwise 200 and the list of tasks
     */
    @GetMapping("/task")
    public ResponseEntity<?> getAllTasks() {
        List<TaskEntity> result = taskRepository.findAll();

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tasks registered");
        } else {
            return ResponseEntity.ok(result);
        }

    }

    /**
     * Get a task by ID
     *
     * @param id
     * @return 404 when missing, otherwise 200 and the task entity found
     */
    @GetMapping("/task/{id}")
    public TaskEntity getTask(@PathVariable Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * Create a new task
     *
     * @param newTask
     * @return 201 and the task entity created
     */
    @PostMapping("/task")
    public ResponseEntity<?> createTask(@RequestBody TaskEntity newTask) {
        TaskEntity task = new TaskEntity();
        task.setDescription(newTask.getDescription());
        task.setCompleted(newTask.isCompleted());
        task.setPriority(newTask.getPriority());
        task.setCreationDate(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(task));
    }

    /**
     * @param updTask
     * @param id
     * @return 200 Ok when success, otherwise 404 when missing a task
     */
    @PutMapping("/task/{id}")
    public TaskEntity updateTask(@RequestBody TaskEntity updTask, @PathVariable Long id) {

        return taskRepository.findById(id).map(p -> {
            p.setDescription(updTask.getDescription());
            p.setCompleted(updTask.isCompleted());
            return taskRepository.save(p);
        }).orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * Delete a task by ID
     *
     * @param id
     * @return 204 with no content
     */
    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(task);
        return ResponseEntity.noContent().build();
    }

}
