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
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
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

        //
        Properties standardProps = loadProperties(1);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> standardEvents = builder.stream(STANDARD_WEATHER_TOPIC);
        KStream<String, String> alertEvents = builder.stream(WEATHER_ALERTS_TOPIC);
        
        // 1. Count temperature readings of standard weather events per weather station.
        KTable<String, Long> outlines = standardEvents
        .groupByKey()
        .count();

        outlines
        .mapValues((k,v) -> {
            //String result = "{\"station\": " + k + ", \"readings\": " + v + "}";
            //writeToFile("1.txt", result);
            return jsonToBD("station", k, "readings", String.valueOf(v), false);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-1", Produced.with(Serdes.String(), Serdes.String()));


        // 2. Count temperature readings of standard weather events per location.
        KTable<String, Long> readingPerLocation = standardEvents
        .selectKey((key, value) -> loadJSONAttribute(value, "location"))
        .groupByKey()
        .count();

        readingPerLocation
        .mapValues((key, value) -> {
            //String result = "{\"location\": " + key + ", \"readings\": " + value + "}";
            //writeToFile("2.txt", result);
            return jsonToBD("location", key, "readings", String.valueOf(value), false);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-2", Produced.with(Serdes.String(), Serdes.String()));


        // 3. Get minimum and maximum temperature per weather station.
        KTable<String, int[]> minMaxTempPerStation = standardEvents
        .groupByKey()
        .aggregate(() -> new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE}, (aggKey, newValue, aggValue) -> {
            int temperature = loadJSONIntAttribute(newValue, "temperature");

            aggValue[0] = Math.min(aggValue[0], temperature);
            aggValue[1] = Math.max(aggValue[1], temperature);

            return aggValue;
        }, Materialized.with(Serdes.String(), new IntArraySerde()));

        minMaxTempPerStation.mapValues((key, values) -> {
            //String result = "{\"station\": " + key + ", \"minTemperature\": " + values[0]  + ", \"maxTemperature\":" + values[1] + "}";
            //writeToFile("3.txt", result);
            return jsonToBDMultipleColumns("station", key, "minTemperature",
             values[0], "maxTemperature", values[1]);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-3", Produced.with(Serdes.String(), Serdes.String()));


        // 4. Get minimum and maximum temperature per location (Students should compute these values in Fahrenheit).
        KTable<String, int[]> minMaxPerLocation = standardEvents
        .selectKey((key, value) -> loadJSONAttribute(value, "location"))
        .groupByKey()
        .aggregate(() -> new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE}, (aggKey, newValue, aggValue) -> {
            int temperature = loadJSONIntAttribute(newValue, "temperature");

            aggValue[0] = Math.min(aggValue[0], temperature);
            aggValue[1] = Math.max(aggValue[1], temperature);

            return aggValue;
        }, Materialized.with(Serdes.String(), new IntArraySerde()));


        minMaxPerLocation.mapValues((key, values) -> {
            //String result = "{\"location\": " + key + ", \"minTemperature\": " + celsiusToFahrenheit(values[0]) + ", \"maxTemperature\":" + celsiusToFahrenheit(values[1]) + "}";
            //writeToFile("4.txt", result);
            return jsonToBDMultipleColumns("location", key, "minTemperature",
             values[0], "maxTemperature", values[1]);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-4", Produced.with(Serdes.String(), Serdes.String()));


        // 5. Count the total number of alerts per weather station.
        alertEvents
        .groupByKey()
        .count()
        .mapValues((k,v) -> {
            //String result = "{\"station\": " + k + ", \"alerts\": " + v + "}";
            //writeToFile("5.txt", result);
            return jsonToBD("station", k, "alerts", String.valueOf(v), false);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-5", Produced.with(Serdes.String(), Serdes.String()));


        // 6. Count the total alerts per type
        alertEvents
        .selectKey((key, value) -> loadJSONAttribute(value, "type"))
        .groupByKey()
        .count()
        .mapValues((key, value) -> {
            //String result = "{\"alertType\": " + key + ", \"readings\": " + value + "}";
            //writeToFile("6.txt", result);
            return jsonToBD("alertType", key, "readings", String.valueOf(value), false);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-6", Produced.with(Serdes.String(), Serdes.String()));


        // 7. Get minimum temperature of weather stations with red alert events.

        // Get weather stations in red zones
        KStream<String, String> weatherStationsWithRedAlerts = alertEvents.filter((key, value) -> {
            return loadJSONAttribute(value, "type").compareTo("red") == 0;
        });

        // Get minimum temperatures per each weather station
        KStream<String, Integer> minTempRedStream = weatherStationsWithRedAlerts.join(minMaxTempPerStation,
            (left, right) -> right[0]
        );

        minTempRedStream
        .groupByKey()
        .reduce((oldValue, newValue) -> newValue, Materialized.with(Serdes.String(), new IntegerSerde()))
        .mapValues((key, value) -> {
            //String result = "{\"redAlertStation\": " + key + ", \"minTemperature\": " + value + "}";
            //writeToFile("7.txt", result);
            return jsonToBD("redAlertStation", key, "minTemperature", String.valueOf(value), false);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-7", Produced.with(Serdes.String(), Serdes.String()));

        // 8. Get maximum temperature of each location of alert events for the last hour

        // Get locations with alerts
        KStream<String, String> locationsWithAlerts = alertEvents
        .selectKey((key, value) -> loadJSONAttribute(value, "location"));

        // Get temperature reading of each location
        KStream<String, String> locationTemperatures = standardEvents
        .selectKey((key, value) -> loadJSONAttribute(value, "location"));

        // Get temperature reading of each alert location
        KStream<String, Integer> alertLocationTemperatures = locationsWithAlerts
        .join(
            locationTemperatures, 
            (left, right) -> loadJSONIntAttribute(right, "temperature"),
             JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5))
        );

        Duration windowSize = Duration.ofMinutes(5);
        Duration gracePeriod = Duration.ofMinutes(1);
        TimeWindows tumblingWindow = TimeWindows.ofSizeAndGrace(windowSize, gracePeriod);

        alertLocationTemperatures
        .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
        .windowedBy(tumblingWindow)
        .aggregate(() -> new int[]{Integer.MIN_VALUE}, (aggKey, newValue, aggValue) -> {
            aggValue[0] = Math.max(aggValue[0], newValue);
            return aggValue;
        }, Materialized.with(Serdes.String(), new IntArraySerde()))
        .toStream((wk, v) -> wk.key())
        .mapValues((key, value) -> {
            //String result = "{\"location\": " + key + ", \"maxTemperature\": " + value[0] + "}";
            //writeToFile("8.txt", result);
            return jsonToBD("location", key, "maxTemperature", String.valueOf(value[0]), false);
        })
        .to(OUTPUT_TOPIC + "-8", Produced.with(Serdes.String(), Serdes.String()));


        // 9. Get minimum temperature per weather station in red alert zones. -> Fix

        // Join alerts and temperature readings of each weather station
        KStream<String, String> stationAlertsAndTemperatures = alertEvents
        .join(
            standardEvents, 
            (left, right) -> (convertToJson("type", loadJSONAttribute(left, "type"), "temperature", loadJSONIntAttribute(right, "temperature"))),
             JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5))
        );

        // Get weather stations in red zones
        KStream<String, String> stationTemperaturesInRed = stationAlertsAndTemperatures.filter((key, value) -> {
            return loadJSONAttribute(value, "type").compareTo("red") == 0;
        });
        
        stationTemperaturesInRed
        .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
        .aggregate(() -> new int[]{Integer.MAX_VALUE}, (aggKey, newValue, aggValue) -> {
            int temperature = loadJSONIntAttribute(newValue, "temperature");
            aggValue[0] = Math.min(aggValue[0], temperature);
            return aggValue;
        }, Materialized.with(Serdes.String(), new IntArraySerde()))
        .mapValues((key, value) -> {
            //String result = "{\"location\": " + key + ", \"maxTemperature\": " + value[0] + "}";
            //writeToFile("8.txt", result);
            return jsonToBD("weatherStation", key, "minTemperature", String.valueOf(value[0]), false);
        })
        .toStream()
        .to(OUTPUT_TOPIC + "-9", Produced.with(Serdes.String(), Serdes.String()));

        // 10. Get the average temperature per weather stations
        KTable<String, int[]> averageTemps = standardEvents
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
                return jsonToBD("weatherStation", key, "avgTemperature", valueDB, true);
            })
            .toStream()
            .to(OUTPUT_TOPIC + "-10", Produced.with(Serdes.String(), Serdes.String()));

        // 11. Get the average temperature of weather stations with red alert events for the last hour

        stationTemperaturesInRed
        .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
        .windowedBy(tumblingWindow)
        .aggregate(() -> new int[]{0, 0}, (aggKey, newValue, aggValue) -> {

            aggValue[0] += 1;
            aggValue[1] += loadJSONIntAttribute(newValue, "temperature");

            return aggValue;
        }, Materialized.with(Serdes.String(), new IntArraySerde())).toStream((wk, v) -> wk.key())
        .mapValues((key, value) -> {
            String result = "";
            String valueDB = "null";

            if (value[0] == 0) {
                result = "{\"station\": " + key + ", \"avgTemperature\": null}";
            } else {
                result = "{\"station\": " + key + ", \"avgTemperature\": " + (1.0 * value[1]) / value[0] + "}";
                valueDB = String.valueOf((1.0 * value[1]) / value[0]);
            }
            return jsonToBD("weatherStation", key, "avgTemperature", valueDB, true);
        })
        .to(OUTPUT_TOPIC + "-11", Produced.with(Serdes.String(), Serdes.String()));

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

    private String convertToJson(String fieldA, String valueA, String fieldB, int valueB) {
        return "{ " +
            fieldA + ": " + valueA + "," +
            fieldB + ": " + valueB 
            + "}";
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
        "\"fields\":[{\"type\":\"string\",\"optional\": false,\"field\":\""+keyColName+"\"},"+
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
