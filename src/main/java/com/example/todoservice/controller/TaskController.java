package com.example.todoservice.controller;

import com.example.todoservice.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final HashMap<Long, Task> tasks = new HashMap<>();

    @GetMapping
    public List<Task> getAllTasks() {
        return tasks.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id " + id.toString());
        }
        return task;
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

}
