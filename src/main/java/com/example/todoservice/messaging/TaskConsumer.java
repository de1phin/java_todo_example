package com.example.todoservice.messaging;

import com.example.todoservice.config.RabbitConfig;
import com.example.todoservice.model.Task;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class TaskConsumer {
    private static final Logger log = LoggerFactory.getLogger(TaskConsumer.class);

    private final ConnectionFactory connectionFactory;
    private final MessageListenerAdapter listenerAdapter;

    public TaskConsumer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.listenerAdapter = new MessageListenerAdapter(this, "handleMessage");
        this.listenerAdapter.setMessageConverter(RabbitConfig.getMessageConverter());
    }

    @PostConstruct
    public void startConsumerThread() {
        Thread thread = new Thread(() -> {
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            container.setQueueNames(RabbitConfig.TASK_QUEUE);
            container.setMessageListener(listenerAdapter);
            container.start();
            log.info("Consumer started");
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void handleMessage(Task task) {
        log.info(">>> [TaskConsumer] Received task: id={}, description={}, status={}",
                task.getId(), task.getDescription(), task.getStatus());
    }
}
