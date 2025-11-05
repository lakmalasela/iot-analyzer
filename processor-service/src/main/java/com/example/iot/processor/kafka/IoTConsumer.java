package com.example.iot.processor.kafka;

import com.example.iot.processor.model.StoredMessage;
import com.example.iot.processor.repository.StoredMessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class IoTConsumer {

    private final StoredMessageRepository repository;

    public IoTConsumer(StoredMessageRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${iot.topic:iot-messages}", groupId = "processor-group")
    public void listen(com.fasterxml.jackson.databind.JsonNode node) {
        try {
            StoredMessage m = new StoredMessage();
            m.setDeviceId(node.get("deviceId").asText());
            m.setTimestamp(node.get("timestamp").asLong());
            m.setTemperature(node.get("temperature").asDouble());
            m.setHumidity(node.get("humidity").asDouble());
            repository.save(m);
            System.out.println("Saved message from device=" + m.getDeviceId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
