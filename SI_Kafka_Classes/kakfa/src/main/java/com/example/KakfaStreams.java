package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serdes.IntegerSerde;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.json.JSONObject;

import com.example.streams.IntArraySerde;

public class KakfaStreams {
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
        KStream<String, String> alerts = builder.stream(WEATHER_ALERTS_TOPIC);

        // // Window
        // Duration windowSize = Duration.ofMinutes(5);
        // Duration advanceSize = Duration.ofMinutes(1);
        // TimeWindows hoppingWindow = TimeWindows.ofSizeWithNoGrace(windowSize).advanceBy(advanceSize);
        

        // 1. Count temperature readings of standard weather events per weather station.
        KTable<String, Long> outlines = lines
        .groupByKey()
        .count();

        outlines
        .mapValues((k,v) -> {
            String result = "{Weather station: " + k + ", Number of readings: " + v + "}";
            writeToFile("1.txt", result);
            return result;
        })
        .toStream()
        .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));


        // 2. Count temperature readings of standard weather events per location.
        KTable<String, Long> readingPerLocation = lines
        .selectKey((key, value) -> loadJSONAttribute(value, "location"))
        .groupByKey()
        .count();

        readingPerLocation
        .mapValues((key, value) -> {
            String result = "{Location: " + key + "; Number of readings: " + value + "}";
            writeToFile("2.txt", result);
            return result;
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-2", Produced.with(Serdes.String(), Serdes.String()));


        // 3. Get minimum and maximum temperature per weather station.
        KTable<String, int[]> minMaxTempPerStation = lines
        .groupByKey()
        .aggregate(() -> new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE}, (aggKey, newValue, aggValue) -> {
            int temperature = loadJSONIntAttribute(newValue, "temperature");

            aggValue[0] = Math.min(aggValue[0], temperature);
            aggValue[1] = Math.max(aggValue[1], temperature);

            return aggValue;
        }, Materialized.with(Serdes.String(), new IntArraySerde()));

        minMaxTempPerStation.mapValues((key, values) -> {
            String result = "{Weather station: " + key + ", minimum temperature: " + values[0]  + ", maximum temperature:" + values[1] + "}";
            writeToFile("3.txt", result);
            return result;
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-3", Produced.with(Serdes.String(), Serdes.String()));


        // 4. Get minimum and maximum temperature per location (Students should compute these values in Fahrenheit).
        lines
        .selectKey((key, value) -> loadJSONAttribute(value, "location"))
        .groupByKey()
        .aggregate(() -> new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE}, (aggKey, newValue, aggValue) -> {
            int temperature = loadJSONIntAttribute(newValue, "temperature");

            aggValue[0] = Math.min(aggValue[0], temperature);
            aggValue[1] = Math.max(aggValue[1], temperature);

            return aggValue;
        }, Materialized.with(Serdes.String(), new IntArraySerde()))
        .mapValues((key, values) -> {
            String result = "{Location: " + key + ", minimum temperature: " + celsiusToFahrenheit(values[0])  + ", maximum temperature:" + celsiusToFahrenheit(values[1]) + "}";
            writeToFile("4.txt", result);
            return result;
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-4", Produced.with(Serdes.String(), Serdes.String()));


        // 5. Count the total number of alerts per weather station.
        alerts
        .groupByKey()
        .count()
        .mapValues((k,v) -> {
            String result = "{Weather station: " + k + ", Number of alerts: " + v + "}";
            writeToFile("5.txt", result);
            return result;
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-5", Produced.with(Serdes.String(), Serdes.String()));

        // 6. Count the total alerts per type
        alerts
        .selectKey((key, value) -> loadJSONAttribute(value, "type"))
        .groupByKey()
        .count()
        .mapValues((key, value) -> {
            String result = "{Type of alert: " + key + "; Number of readings: " + value + "}";
            writeToFile("6.txt", result);
            return result;
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-6", Produced.with(Serdes.String(), Serdes.String()));


        // 7. Get minimum temperature of weather stations with red alert events.

        // Get weather stations in red zones
        KStream<String, String> weatherStationsWithRedAlerts = alerts.filter((key, value) -> {
            return loadJSONAttribute(value, "type").compareTo("red") == 0;
        });

        // Get minimum temperatures per each weather station
        KStream<String, Integer> minTempRedStream = weatherStationsWithRedAlerts.join(minMaxTempPerStation,
            (left, right) -> right[0]
        );

        minTempRedStream
        .groupByKey()
        .aggregate(() -> Integer.MAX_VALUE, (aggKey, newValue, aggValue) -> {
            return newValue;
        }, Materialized.with(Serdes.String(), new IntegerSerde()))
        .mapValues((key, value) -> {
            String result = "{Minimum temperature of weather stations: " + value + "}";
            writeToFile("7.txt", result);
            return result;
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-7", Produced.with(Serdes.String(), Serdes.String()));

        // 9. Get minimum temperature per weather station in red alert zones.
        
        minTempRedStream
        .groupByKey()
        .aggregate(() -> new int[]{Integer.MAX_VALUE}, (aggKey, newValue, aggValue) -> {
            aggValue[0] = Math.min(aggValue[0], newValue);
            return aggValue;
        }, Materialized.with(Serdes.String(), new IntArraySerde()))
        .mapValues((key, value) -> {
            String result = "{Weather station with red alert: " + key + ", minimum temperature: " + value[0] + "}";
            writeToFile("9.txt", result);
            return result;
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-9", Produced.with(Serdes.String(), Serdes.String()));


        // 10. Get the average temperature per weather stations
        lines
            .groupByKey()
            .aggregate(() -> new int[]{0, 0}, (aggKey, newValue, aggValue) -> {

                aggValue[0] += 1;
                aggValue[1] += loadJSONIntAttribute(newValue, "temperature");

                return aggValue;
            }, Materialized.with(Serdes.String(), new IntArraySerde()))
            .mapValues((key, v) -> {
                String result = "";
                if (v[0] == 0) {
                    result = "{Weather station: " + key + ", average temperature: div by 0}";
                } else {
                    result = "{Weather station: " + key + ", average temperature: " + (1.0 * v[1]) / v[0] + "}";
                }
                writeToFile("10.txt", result);
                return result;
            })
            .toStream()
            .to(OUTPUT_TOPIC + "-10", Produced.with(Serdes.String(), Serdes.String()));



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

    private String loadJSONAttribute(String data, String parameter) {
        JSONObject json = new JSONObject(data);
        return json.getString(parameter);
    }

    private int loadJSONIntAttribute(String data, String parameter) {
        JSONObject json = new JSONObject(data);
        return json.getInt(parameter);
    }

    private void writeToFile(String filename, String text) {
        try {
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private float celsiusToFahrenheit(int celsius) {
        return ((celsius * 9) / 5) + 32;
    }
}
