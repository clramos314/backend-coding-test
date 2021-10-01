package com.example.demo.task.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.task.error.SearchTaskNoResultException;
import com.example.demo.task.error.TaskNotFoundException;
import com.example.demo.task.model.TaskEntity;
import com.example.demo.task.model.TaskRepository;
import com.example.demo.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
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

    @GetMapping(value = "/task")
    public ResponseEntity<?> searchTasksByParams(
            @RequestParam("priority") Optional<Integer> priority,
            @RequestParam("completed") Optional<Boolean> completed,
            @RequestParam(defaultValue = "id,desc") String sort) {

        List<Order> orders = new ArrayList<>();

        if (sort.contains(",")) {
            String[] s_slide = sort.split(",");
            orders.add(new Order(getSortDirection(s_slide[1]), s_slide[0]));
        }

        List<TaskEntity> result = taskService.findByParamsOrderedBy(priority, completed, Sort.by(orders));

        if (result.isEmpty()) {
            throw new SearchTaskNoResultException();
        } else {
            return ResponseEntity.ok(result);
        }

    }

}
