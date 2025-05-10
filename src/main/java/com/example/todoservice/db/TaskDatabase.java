package com.example.todoservice.db;


import com.example.todoservice.db.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDatabase extends JpaRepository<TaskEntity, Long> {}
