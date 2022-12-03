package com.example.Models;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public final class CustomSerdes {

    private CustomSerdes() {}

    public static Serde<StandardEvent> StandardEvent() {
        JsonSerializer<StandardEvent> serializer = new JsonSerializer<>();
        JsonDeserializer<StandardEvent> deserializer = new JsonDeserializer<>(StandardEvent.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
        
    public static Serde<AlertEvent> GenreCount() {
        JsonSerializer<AlertEvent> serializer = new JsonSerializer<>();
        JsonDeserializer<AlertEvent> deserializer = new JsonDeserializer<>(AlertEvent.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}
