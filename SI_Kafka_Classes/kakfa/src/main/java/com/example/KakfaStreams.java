package com.example;

import java.io.FileWriter;
import java.io.IOException;
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
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.json.JSONObject;

import com.example.streams.IntArraySerde;

public class KakfaStreams {
    private final int SLEEP_TIME = 2000;
    private final String STANDARD_WEATHER_TOPIC = "StandardWeatherTopic", WEATHER_ALERTS_TOPIC = "WeatherAlertsTopic";
    private final String OUTPUT_TOPIC = "Hello";

    public KakfaStreams() {
        myMain();
    }
    
    public static void main(String[] args) throws Exception {
        new KakfaStreams();
    }

    private void myMain() {

        Properties standardProps = loadProperties(1);
        // Properties alertProps = loadProperties(2);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> lines = builder.stream(STANDARD_WEATHER_TOPIC);

        // // Window
        // Duration windowSize = Duration.ofMinutes(5);
        // Duration advanceSize = Duration.ofMinutes(1);
        // TimeWindows hoppingWindow = TimeWindows.ofSizeWithNoGrace(windowSize).advanceBy(advanceSize);


        // while(true){ 

        
        KTable<String, Long> outlines;
        /*
         * 1. Count temperature readings of standard weather events per weather station.
         */
        outlines = lines.groupByKey().count();
        outlines.mapValues((k,v) -> {
            System.out.println("GOD IS DEAD");

            String res = k + " -> " + v;

            try {
                FileWriter myWriter = new FileWriter("al1.txt");
                myWriter.write(res);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            return res;
        }).toStream().to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
    
        // /*
        //  * 2. Count temperature readings of standard weather events per location.
        //  */
        // outlines = lines.groupByKey().count();
        // outlines.mapValues(v -> "" + v).toStream().to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));


        KafkaStreams streams = new KafkaStreams(builder.build(), standardProps);
        streams.start();
    }

    private Properties loadProperties(int consumerID) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-stream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "broker1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        return props;
    }

    private int loadTemperature(String key, String data) {
        JSONObject json = new JSONObject(data);
        System.out.println(key + " " + data);
        return json.getInt("temperature");
    }
}
