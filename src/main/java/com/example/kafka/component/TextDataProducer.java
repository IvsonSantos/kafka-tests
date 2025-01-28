package com.example.kafka.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Slf4j
@Component
public class TextDataProducer {

    Logger logger = Logger.getLogger(getClass().getName());

    // Constants for topic configuration
    private final static int PARTITION_COUNT = 8;
    private final static String TOPIC = "TEXT-DATA";
    private final static short REPLICATION_FACTOR = 1;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TextDataProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * It creates or modifies the topic with the specified settings.
     */
    @Autowired
    public void configureTopic(KafkaAdmin kafkaAdmin) {
        kafkaAdmin.createOrModifyTopics(new NewTopic(TOPIC, PARTITION_COUNT, REPLICATION_FACTOR));
    }

    /**
     * sends a text message to the Kafka topic, distributing messages across partitions based on the line index.
     */
    private void sendTextMessage(String text, int lineIndex) {
        if (text == null || text.isEmpty()) {
            return;
        }
        // Sends the Link message to the topic, distributing across partitions based on the line index
        kafkaTemplate.send(TOPIC, "KEY-" + (lineIndex % PARTITION_COUNT), text);
    }

    /**
     * responsible for reading the content of the given file, line by line, and sending each
     * line as a message to the Kafka topic named TEXT-DATA.
     */
    public void sendContentOf(File file) {
        Instant before = Instant.now();
        try (Stream<String> lines = Files.lines(file.toPath())) {
            AtomicInteger counter = new AtomicInteger(0);
            lines.forEach(line ->
                sendTextMessage(line, counter.getAndIncrement())
            );
            Instant after = Instant.now();
            Duration duration = Duration.between(before, after);
            //logger.info("Streamed %s lines in %s millisecond", counter.get(), duration.toMillis());
            //log.info("Streamed {} lines in {} millisecond", counter.get(), duration.toMillis() );
            System.out.println("Streamed {} lines in {} millisecond" + counter.get() + duration.toMillis());
            //log.info("Streamed {} lines in {} millisecond", counter.get(), duration.toMillis() );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
