package com.example.todoservice.controller;

import com.example.todoservice.db.ModelConverter;
import com.example.todoservice.db.TaskDatabase;
import com.example.todoservice.db.entity.TaskEntity;
import com.example.todoservice.messaging.TaskProducer;
import com.example.todoservice.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final TaskProducer taskProducer;
    private final TaskDatabase taskDatabase;

    public TaskController(TaskProducer taskProducer, TaskDatabase taskDatabase) {
        this.taskProducer = taskProducer;
        this.taskDatabase = taskDatabase;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskDatabase.findAll().stream().map(ModelConverter::taskModel).toList();
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        Optional<TaskEntity> taskEntity = taskDatabase.findById(id);
        if (taskEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id " + id.toString());
        }
        return ModelConverter.taskModel(taskEntity.get());
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        taskDatabase.save(ModelConverter.taskEntity(task));
        taskProducer.send(task);
        return task;
    }

}
