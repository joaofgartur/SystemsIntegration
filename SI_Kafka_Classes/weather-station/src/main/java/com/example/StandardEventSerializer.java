package com.example;

import java.nio.ByteBuffer;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class StandardEventSerializer implements Serializer<StandardEvent> {
    private String encoding = "UTF8";

    @Override
    public byte[] serialize(String topic, StandardEvent event) {

        int sizeOfEventLocation;
        int sizeOfEventTemperature;
        byte[] serializedLocation;
        byte[] serializedTemperature;

        try {
            if (event == null) {
                return null;
            }

            serializedLocation = event.getLocation().getBytes(encoding);
            sizeOfEventLocation = serializedLocation.length;

            String temperature = String.valueOf(event.getTemperature());
            serializedTemperature = temperature.getBytes(encoding);
            sizeOfEventTemperature = serializedTemperature.length;

            ByteBuffer buffer = ByteBuffer.allocate(4+4+sizeOfEventLocation+4+sizeOfEventTemperature);

            buffer.putInt(sizeOfEventLocation);
            buffer.put(serializedLocation);
            buffer.putInt(sizeOfEventTemperature);
            buffer.put(serializedTemperature);

            return buffer.array();
            
        } catch (Exception e) {
            throw new SerializationException("Error when serializing StandardEvent to byte[]");
        }
    }
    
}
