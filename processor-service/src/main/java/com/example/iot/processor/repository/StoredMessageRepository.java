package com.example.iot.processor.repository;

import com.example.iot.processor.model.StoredMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoredMessageRepository extends MongoRepository<StoredMessage, String> {
}
