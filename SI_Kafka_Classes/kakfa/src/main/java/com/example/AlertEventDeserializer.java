package com.example;

import java.nio.ByteBuffer;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class AlertEventDeserializer implements Deserializer<AlertEvent> {
    private String encoding = "UTF8";

    @Override
    public AlertEvent deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null recieved at deserialize");
                return null;
            }
            
            ByteBuffer buffer = ByteBuffer.wrap(data);

            int sizeOfLocation = buffer.getInt();
            byte[] locationBytes = new byte[sizeOfLocation];
            buffer.get(locationBytes);
            String deserializedLocation = new String(locationBytes, encoding);

            int sizeOfType = buffer.getInt();
            byte[] typeBytes = new byte[sizeOfType];
            buffer.get(typeBytes);
            String deserializedType = new String(typeBytes,encoding);

            return new AlertEvent(deserializedLocation, deserializedType);

        } catch (Exception e) {
            throw new SerializationException("Error when deserialize byte[] to Employee");
        }
    }

    
    
}
