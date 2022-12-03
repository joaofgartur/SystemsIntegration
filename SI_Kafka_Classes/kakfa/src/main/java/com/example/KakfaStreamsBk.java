package com.example;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.json.JSONObject;

import com.example.streams.IntArraySerde;

public class KakfaStreamsBk {
    private final int SLEEP_TIME = 2000;
    private final String STANDARD_WEATHER_TOPIC = "StandardWeatherTopic", WEATHER_ALERTS_TOPIC = "WeatherAlertsTopic";
    private final String OUTPUT_TOPIC = "Hello";

    public KakfaStreamsBk() {
        myMain();
    }
    
    public static void main(String[] args) throws Exception {
        new KakfaStreamsBk();
    }

    private void myMain() {

        Properties standardProps = loadProperties(1);
        Properties alertProps = loadProperties(2);

        StreamsBuilder builder = new StreamsBuilder();

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(standardProps);
        consumer.subscribe(Collections.singletonList(STANDARD_WEATHER_TOPIC));
        System.out.print("consumer");
        Duration d = Duration.ofSeconds(10);
        
        try {
                KStream<String, String> standardEvents = builder.stream(STANDARD_WEATHER_TOPIC);
                KStream<String, String> alertEvents = builder.stream(WEATHER_ALERTS_TOPIC);

                /* aggregate() */
                standardEvents
                .groupByKey()
                .aggregate(() -> new int[]{0, 0}, (aggKey, newValue, aggValue) -> {
                    System.out.println("Oi");
                    aggValue[0] += 1;
                    aggValue[1] += loadTemperature(aggKey, newValue);

                    return aggValue;
                }, Materialized.with(Serdes.String(), new IntArraySerde()))
                .mapValues(v -> v[0] != 0 ? "" + (1.0 * v[1]) / v[0] : "div by 0")
                .toStream()
                .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("OUTPUT_TOPIC");
                
                KafkaStreams streams = new KafkaStreams(builder.build(), standardProps);
                streams.start();
        } finally {
            System.out.println("Finally!");
            consumer.close();
        }
    }

    private Properties loadProperties(int consumerID) {
        Properties props = new Properties();
        // props.put("bootstrap.servers", "broker1:9092");
        // props.put("retries", 0);
        // props.put("batch.size", 16384);
        // props.put("linger.ms", 1);
        // props.put("buffer.memory", 33554432);
        // props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-stream");
        // props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        // props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        // props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        // props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-stream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "broker1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        return props;
    }

    private int loadTemperature(String key, String data) {
        JSONObject json = new JSONObject(data);
        System.out.println(key + " " + data);
        return json.getInt("temperature");
    }
}
