package com.example.todoservice.metrics;

import com.example.todoservice.model.TaskStatus;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TaskMetrics {
    private final MeterRegistry registry;

    public TaskMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void incTasksCreated(TaskStatus status) {
        registry.counter("tasks_created", "status", status.name())
                .increment();
    }
}
