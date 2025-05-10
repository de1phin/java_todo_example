package com.example.todoservice.db;

import com.example.todoservice.db.entity.TaskEntity;
import com.example.todoservice.db.entity.TaskStatusEntity;
import com.example.todoservice.model.Task;
import com.example.todoservice.model.TaskStatus;

public class ModelConverter {
    public static Task taskModel(TaskEntity entity) {
        Task task = new Task();
        task.setId(entity.getId());
        task.setDescription(entity.getDescription());
        task.setStatus(TaskStatus.valueOf(entity.getStatus().toString()));
        return task;
    }

    public static TaskEntity taskEntity(Task task) {
        TaskEntity entity = new TaskEntity();
        entity.setId(task.getId());
        entity.setDescription(task.getDescription());
        entity.setStatus(TaskStatusEntity.valueOf(task.getStatus().toString()));
        return entity;
    }
}
