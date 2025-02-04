package com.example.kafka.producer;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Component
public class TextDataProducer {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final static int PARTITION_COUNT = 8;
    private final static String TOPIC = "text-data-topic";
    private final static short REPLICATION_FACTOR = 1;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public TextDataProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void configureTopic(KafkaAdmin kafkaAdmin) {
        kafkaAdmin.createOrModifyTopics(new NewTopic(TOPIC, PARTITION_COUNT, REPLICATION_FACTOR));
    }

    private void sendTextMessage(String text, int lineIndex) {
        if (text == null || text.isEmpty()) {
            return;
        }
        logger.info("Sending message: {}", text);
        kafkaTemplate.send(TOPIC, "KEY-" + (lineIndex % PARTITION_COUNT), text);
    }

    public void sendContentOf(File file) {
        Instant before = Instant.now();

        try (Stream<String> lines = Files.lines(file.toPath())) {
            AtomicInteger counter = new AtomicInteger();
            lines.forEach(line -> sendTextMessage(line, counter.getAndIncrement()));
            Instant after = Instant.now();
            Duration duration = Duration.between(before, after);
            logger.info("Streamed {} lines in {} millisecond ", counter.get(), duration.toMillis());
        } catch (Exception e) {
            logger.error("Error while reading file: {}", file.getName(), e);
        }
    }

}
