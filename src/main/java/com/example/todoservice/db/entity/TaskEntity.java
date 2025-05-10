package com.example.todoservice.db.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tasks")
@Data
public class TaskEntity {

    @Id
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatusEntity status;
}
