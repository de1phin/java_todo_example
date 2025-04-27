package com.example.todoservice.model;

import lombok.Data;

@Data
public class Task {
    private Long id;
    private String description;
    private TaskStatus status;
}
