package com.example.iot.ingest.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class IoTProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public IoTProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, Object data) {
        kafkaTemplate.send(topic, data);
    }
}
