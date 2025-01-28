package com.example.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * consumes the messages from the Kafka topic of the Spring application
 */
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "my_topic", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Message received: " + message);
    }

}
