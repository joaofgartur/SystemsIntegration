package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serdes.IntegerSerde;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.json.JSONObject;

import com.example.streams.IntArraySerde;

public class KakfaStreams {
    private final String STANDARD_WEATHER_TOPIC = "StandardWeatherTopic", WEATHER_ALERTS_TOPIC = "WeatherAlertsTopic";
    private final String OUTPUT_TOPIC = "results";

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
            String result = "{\"station\": " + k + ", \"readings\": " + v + "}";
            writeToFile("1.txt", result);
            return jsonToBD("station", k, "readings", String.valueOf(v), false);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-1", Produced.with(Serdes.String(), Serdes.String()));


        // 2. Count temperature readings of standard weather events per location.
        KTable<String, Long> readingPerLocation = lines
        .selectKey((key, value) -> loadJSONAttribute(value, "location"))
        .groupByKey()
        .count();

        readingPerLocation
        .mapValues((key, value) -> {
            String result = "{\"location\": " + key + ", \"readings\": " + value + "}";
            writeToFile("2.txt", result);
            return jsonToBD("location", key, "readings", String.valueOf(value), false);
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
            String result = "{\"station\": " + key + ", \"minTemperature\": " + values[0]  + ", \"maxTemperature\":" + values[1] + "}";
            writeToFile("3.txt", result);
            return jsonToBDMultipleColumns("station", key, "minTemperature", values[0], "maxTemperature", values[1]);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-3", Produced.with(Serdes.String(), Serdes.String()));


        // 4. Get minimum and maximum temperature per location (Students should compute these values in Fahrenheit).
        KTable<String, int[]> minMaxPerLocation = lines
        .selectKey((key, value) -> loadJSONAttribute(value, "location"))
        .groupByKey()
        .aggregate(() -> new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE}, (aggKey, newValue, aggValue) -> {
            int temperature = loadJSONIntAttribute(newValue, "temperature");

            aggValue[0] = Math.min(aggValue[0], temperature);
            aggValue[1] = Math.max(aggValue[1], temperature);

            return aggValue;
        }, Materialized.with(Serdes.String(), new IntArraySerde()));


        minMaxPerLocation.mapValues((key, values) -> {
            String result = "{\"location\": " + key + ", \"minTemperature\": " + celsiusToFahrenheit(values[0]) + ", \"maxTemperature\":" + celsiusToFahrenheit(values[1]) + "}";
            writeToFile("4.txt", result);
            return jsonToBDMultipleColumns("location", key, "minTemperature", values[0], "maxTemperature", values[1]);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-4", Produced.with(Serdes.String(), Serdes.String()));


        // 5. Count the total number of alerts per weather station.
        alerts
        .groupByKey()
        .count()
        .mapValues((k,v) -> {
            String result = "{\"station\": " + k + ", \"alerts\": " + v + "}";
            writeToFile("5.txt", result);
            return jsonToBD("station", k, "alerts", String.valueOf(v), false);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-5", Produced.with(Serdes.String(), Serdes.String()));

        // 6. Count the total alerts per type
        alerts
        .selectKey((key, value) -> loadJSONAttribute(value, "type"))
        .groupByKey()
        .count()
        .mapValues((key, value) -> {
            String result = "{\"alertType\": " + key + ", \"readings\": " + value + "}";
            writeToFile("6.txt", result);
            return jsonToBD("alertType", key, "readings", String.valueOf(value), false);
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
        .aggregate(() -> 0, (aggKey, newValue, aggValue) -> {
            return newValue;
        }, Materialized.with(Serdes.String(), new IntegerSerde()))
        .mapValues((key, value) -> {
            String result = "{\"redAlertStation\": " + key + ", \"minTemperature\": " + value + "}";
            writeToFile("7.txt", result);
            return jsonToBD("redAlertStation", key, "minTemperature", String.valueOf(value), false);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-7", Produced.with(Serdes.String(), Serdes.String()));


        // 8. Get maximum temperature of each location of alert events for the last hour

        // Get locations with red alerts
        KStream<String, String> locationsWithRedAlerts = alerts
        .selectKey((key, value) -> loadJSONAttribute(value, "location"))
        .filter((key, value) -> {
            return loadJSONAttribute(value, "type").compareTo("red") == 0;
        });

        KStream<String, Integer> maxTempRedLocation = locationsWithRedAlerts.join(minMaxPerLocation,
            (left, right) -> right[1]
        );

        Duration windowSize = Duration.ofMinutes(5);
        Duration gracePeriod = Duration.ofMinutes(1);
        TimeWindows tumbling = TimeWindows.ofSizeAndGrace(windowSize, gracePeriod);

        maxTempRedLocation
        .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
        .windowedBy(tumbling)
        .reduce((oldValue, newValue) -> newValue)
        .toStream((wk, v) -> wk.key())
        .mapValues((key, value) -> {
            String result = "{\"location\": " + key + ", \"maxTemperature\": " + value + "}";
            writeToFile("8.txt", result);
            return jsonToBD("location", key, "maxTemperature", String.valueOf(value), false);
        })
        .to(OUTPUT_TOPIC + "-8", Produced.with(Serdes.String(), Serdes.String()));

        // 9. Get minimum temperature per weather station in red alert zones. -> Fix

        // Get temperature readings of each location
        KStream<String, Integer> locationTemps = lines
        .selectKey((key, value) -> loadJSONAttribute(value, "location"))
        .mapValues((key, value) -> loadJSONIntAttribute(value, "temperature"));

        // Get alerts of each location
        KStream<String, String> locationAlerts = alerts
        .selectKey((key, value) -> loadJSONAttribute(value, "location"))
        .mapValues((key, value) -> loadJSONAttribute(value, "type"));

        // 10. Get the average temperature per weather stations
        KTable<String, int[]> averageTemps = lines
            .groupByKey()
            .aggregate(() -> new int[]{0, 0}, (aggKey, newValue, aggValue) -> {

                aggValue[0] += 1;
                aggValue[1] += loadJSONIntAttribute(newValue, "temperature");

                return aggValue;
            }, Materialized.with(Serdes.String(), new IntArraySerde()));

            averageTemps
            .mapValues((key, v) -> {
                String result = "";
                String valueDB = "null";

                if (v[0] == 0) {
                    result = "{\"station\": " + key + ", \"avgTemperature\": null}";
                } else {
                    result = "{\"station\": " + key + ", \"avgTemperature\": " + (1.0 * v[1]) / v[0] + "}";
                    valueDB = String.valueOf((1.0 * v[1]) / v[0]);
                }
                writeToFile("10.txt", result);
                return jsonToBD("station", key, "avgTemperature", valueDB, true);
            })
            .toStream()
            .to(OUTPUT_TOPIC + "-10", Produced.with(Serdes.String(), Serdes.String()));

        // 11. Get the average temperature of weather stations with red alert events for the last hour

        KStream<String, Double> avgRedStream = weatherStationsWithRedAlerts.join(averageTemps, (left, right) -> (right[0] != 0 ? (1.0 * right[1]) / right[0] : 0));

        //avgRedStream.peek((key,value)-> System.out.println("7- Minimum temperature in alert stations " + String.valueOf(value)));

        
 

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


    private String jsonToBD(String keyColName, String key, String valueColName, String value, boolean isDouble){
        String type = "int32";
        if(isDouble) {
            type = "double";
        }

        return "{\"schema\":{\"type\":\"struct\"," + 
        "\"fields\":[{\"type\":\"string\",\"optional\":false,\"field\":\""+keyColName+"\"},"+
        "{\"type\":\""+type+"\",\"optional\": false,\"field\":\""+valueColName+"\"}],\"optional\":false},"+
        "\"payload\":{\""+ keyColName +"\":\""+key+"\",\"" + valueColName + "\": "+value+"}}";
    }

    private String jsonToBDMultipleColumns(String keyColName, String key, String valueColName1, int value1, String valueColName2, int value2 ) {

        return "{\"schema\":{\"type\":\"struct\"," +
                "\"fields\":[{\"type\":\"string\",\"optional\":false,\"field\":\"" + keyColName + "\"}," +
                "{\"type\":\"double\",\"optional\": false,\"field\":\"" + valueColName1+ "\"}," +
                "{\"type\":\"double\",\"optional\": false,\"field\":\"" + valueColName2+ "\"}],\"optional\":false}," +
                "\"payload\":{\"" + keyColName + "\":\"" + key + "\",\"" + valueColName1 + "\": " + value1 + ",\"" +
                valueColName2 + "\": " + value2 + "}}";
    }
}
