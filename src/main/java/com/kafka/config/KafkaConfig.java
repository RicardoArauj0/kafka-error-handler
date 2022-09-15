package com.kafka.config;

import com.kafka.exception.FailedToSaveException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.util.backoff.FixedBackOff;


@Configuration
@EnableKafka
@Slf4j
public class KafkaConfig {

    @Bean
    public DefaultErrorHandler errorHandler(
            KafkaOperations<Object, Object> operations) {

        // Recover will post the message on the DLT topic after all the attempts doesn't succeed
        var recover = new DeadLetterPublishingRecoverer(operations,
                (cr, e) -> new TopicPartition(cr.topic() + ".DLT", 0));

        //var fixedBackOff = new FixedBackOff(1000L, 3);
        var exponentialBackoff = new ExponentialBackOffWithMaxRetries(3);
        exponentialBackoff.setInitialInterval(1000L);
        exponentialBackoff.setMultiplier(3.0);
        exponentialBackoff.setMaxInterval(3000L);

        //Exception to ignore and not retry
        //errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);

        //Exception to retry
        //errorHandler.addRetryableExceptions(FailedToSaveException.class);

        var errorHandler = new DefaultErrorHandler(recover, exponentialBackoff);

        errorHandler.setRetryListeners(((consumerRecord, e, i) ->
                log.error("Failed message: {} with exception: {}. AttemptNumber: {} ", consumerRecord, e, i)));

        return errorHandler;
    }
}
