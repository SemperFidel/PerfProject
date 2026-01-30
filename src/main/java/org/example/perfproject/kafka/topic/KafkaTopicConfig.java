package org.example.perfproject.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Конфигурация топиков для Кафки.
 */
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic balancesTopic() {
        return TopicBuilder.name("balances-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
