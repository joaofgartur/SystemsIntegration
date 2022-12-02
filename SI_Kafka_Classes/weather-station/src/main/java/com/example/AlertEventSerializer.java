package com.example;

import java.nio.ByteBuffer;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class AlertEventSerializer implements Serializer<AlertEvent> {
    private String encoding = "UTF8";

    @Override
    public byte[] serialize(String topic, AlertEvent event) {

        int sizeOfEventLocation;
        int sizeOfEventType;
        byte[] serializedLocation;
        byte[] serializedType;

        try {
            if (event == null) {
                return null;
            }

            serializedLocation = event.getLocation().getBytes(encoding);
            sizeOfEventLocation = serializedLocation.length;

            serializedType = event.getType().getBytes(encoding);
            sizeOfEventType = serializedType.length;

            ByteBuffer buffer = ByteBuffer.allocate(4+4+sizeOfEventLocation+4+sizeOfEventType);

            buffer.putInt(sizeOfEventLocation);
            buffer.put(serializedLocation);
            buffer.putInt(sizeOfEventType);
            buffer.put(serializedType);

            return buffer.array();
            
        } catch (Exception e) {
            throw new SerializationException("Error when serializing AlertEvent to byte[]");
        }
    }
    
}
