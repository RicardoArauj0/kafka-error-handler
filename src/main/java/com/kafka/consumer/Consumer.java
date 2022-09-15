package com.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.service.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer {

    @Autowired
    MessageProcessor messageProcessor;

    @KafkaListener(topics = "${spring.kafka.consumer.topic}")
    public void onMessage(String message) throws JsonProcessingException {
        log.info("Consumed: {}", message);
        messageProcessor.processMessage(message);
    }
}
