package com.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageProcessor {

    public void processMessage(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = objectMapper.readValue(message, Person.class);
        log.info("Processed message {}", person);
    }

}
