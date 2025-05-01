package com.example.todoservice.messaging;

import com.example.todoservice.config.RabbitConfig;
import com.example.todoservice.model.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

@Service
public class TaskProducer {

        private final RabbitTemplate rabbitTemplate;

        public TaskProducer(RabbitTemplate rabbitTemplate) {
            this.rabbitTemplate = rabbitTemplate;
            this.rabbitTemplate.setMessageConverter(RabbitConfig.getMessageConverter());
        }

        public void send(Task task) {
            rabbitTemplate.convertAndSend(RabbitConfig.TASK_QUEUE, task);
        }
}
