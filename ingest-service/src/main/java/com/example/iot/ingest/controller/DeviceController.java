package com.example.iot.ingest.controller;

import com.example.iot.ingest.kafka.IoTProducer;
import com.example.iot.ingest.model.IoTMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    @Autowired
    private IoTProducer producer;

    @PostMapping("/ingest")
    public ResponseEntity<String> ingest(@RequestBody IoTMessage message) {
        producer.send("iot-topic", message); // specify Kafka topic here
        return ResponseEntity.accepted().body("accepted");
    }

}
