package com.example;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

public class KakfaStreams {
    private final String STANDARD_WEATHER_TOPIC = "StandardWeatherTopic", WEATHER_ALERTS_TOPIC = "WeatherAlertsTopic";

    public KakfaStreams() {
        myMain();
    }
    
    public static void main(String[] args) throws Exception {
        new KakfaStreams();
    }

    private void myMain() {

        Properties standardProps = loadProperties(false);
        Properties alertProps = loadProperties(true);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, StandardEvent> standardStream = builder.stream(STANDARD_WEATHER_TOPIC);
        KStream<String, AlertEvent> eventsAlert = builder.stream(WEATHER_ALERTS_TOPIC);
        
        /*
        try {
            while (true) {

                
                Duration d = Duration.ofSeconds(1000000);

                ConsumerRecords<String, StandardEvent> standardRecords = standardConsumer.poll(d);
                for (ConsumerRecord<String, StandardEvent> record : standardRecords) {
                    System.out.println("[STANDARD]" + record.key() + " => " + record.value().getLocation() + ": " + record.value().getTemperature()); 
                }

                ConsumerRecords<String, AlertEvent> alertRecords = alertsConsumer.poll(d);
                for (ConsumerRecord<String, AlertEvent> record : alertRecords) {
                    System.out.println("[ALERT]" + record.key() + " => " + record.value().getLocation() + ": " + record.value().getType()); 
                }
                

            }    
        } finally {
            standardConsumer.close();
            alertsConsumer.close();
        }
        */
    }

    private Properties loadProperties(boolean alertEvent) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "broker1:9092");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        if(alertEvent) {
            props.put("value.deserializer", AlertEventDeserializer.class);
        } else {
            props.put("value.deserializer", StandardEventDeserializer.class);
        }

        return props;
    }
}
