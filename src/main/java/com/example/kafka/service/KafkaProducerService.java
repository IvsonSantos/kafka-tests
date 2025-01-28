package com.example.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * sends the messages to the Kafka topic of the Spring application
 */
@Slf4j
@Service
public class KafkaProducerService {

    private static final String TOPIC = "my_topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Message sent: " + message);
    }

}
