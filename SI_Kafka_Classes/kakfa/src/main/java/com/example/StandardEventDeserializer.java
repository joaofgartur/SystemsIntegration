package com.example;

import java.nio.ByteBuffer;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class StandardEventDeserializer implements Deserializer<StandardEvent> {
    private String encoding = "UTF8";

    @Override
    public StandardEvent deserialize(String topic, byte[] data) {
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

            int sizeOfTemperature = buffer.getInt();
            byte[] temperatureBytes = new byte[sizeOfTemperature];
            buffer.get(temperatureBytes);
            String temperature = new String(temperatureBytes,encoding);

            return new StandardEvent(deserializedLocation, Integer.parseInt(temperature));

        } catch (Exception e) {
            throw new SerializationException("Error when deserialize byte[] to Employee");
        }
    }
    
}
