package com.example.todoservice.controller;

import com.example.todoservice.db.ModelConverter;
import com.example.todoservice.db.TaskDatabase;
import com.example.todoservice.db.entity.TaskEntity;
import com.example.todoservice.messaging.TaskProducer;
import com.example.todoservice.metrics.TaskMetrics;
import com.example.todoservice.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final TaskProducer taskProducer;
    private final TaskDatabase taskDatabase;
    private final TaskMetrics taskMetrics;

    public TaskController(TaskProducer taskProducer, TaskDatabase taskDatabase, TaskMetrics taskMetrics) {
        this.taskProducer = taskProducer;
        this.taskDatabase = taskDatabase;
        this.taskMetrics = taskMetrics;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        log.warn("get all tasks"); // WARN чисто ради разнообразия логов
        return taskDatabase.findAll().stream().map(ModelConverter::taskModel).toList();
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        log.info("get task " + id.toString());
        Optional<TaskEntity> taskEntity = taskDatabase.findById(id);
        if (taskEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id " + id.toString());
        }
        return ModelConverter.taskModel(taskEntity.get());
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        log.info("create task " + ModelConverter.taskEntity(task).toString());
        taskMetrics.incTasksCreated(task.getStatus());
        taskDatabase.save(ModelConverter.taskEntity(task));
        taskProducer.send(task);
        return task;
    }

}
