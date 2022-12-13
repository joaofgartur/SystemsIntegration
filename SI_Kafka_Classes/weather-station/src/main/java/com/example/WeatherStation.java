package com.example;

import java.io.FileReader;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.example.Models.AlertEvent;
import com.example.Models.StandardEvent;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;

public class WeatherStation {

    private final int MIN_TEMPERATURE = -100, MAX_TEMPERATURE = 100, SLEEP_TIME = 2000;
    private final String STANDARD_WEATHER_TOPIC = "StandardWeatherTopic", WEATHER_ALERTS_TOPIC = "WeatherAlertsTopic";
    private final boolean TESTING = false;

    public WeatherStation() {
        myMain();
    }

    public static void main(String[] args) throws Exception {
        new WeatherStation();
    }

    private void myMain() {
        Properties producerProperties = loadPProducerProperties();
        Producer<String, String> producer = new KafkaProducer<>(producerProperties);

        Properties consumerProperties = loadConsumerProperties();
        Consumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(Arrays.asList("locationInfo", "stationInfo"));
        
        // String[] weatherStations = {"Coimbra", "Lisboa", "Porto"};
        // String[] locations = {"Alta", "Baixa", "Polo II", "Norton"};
        ArrayList<String> weatherStations = new ArrayList<>(3);
        ArrayList<String> locations = new ArrayList<>(3);
        String[] alertTypes = {"red", "green"};
        
        if(TESTING){
            String resourceName = "data.json";
            InputStream is = WeatherStation.class.getResourceAsStream(resourceName);
            if (is == null) {
                throw new NullPointerException("Cannot find resource file " + resourceName);
            }

            JSONTokener tokener = new JSONTokener(is);
            JSONObject jsonObject = new JSONObject(tokener);

            JSONArray solutions = (JSONArray) jsonObject.get("events");

            try {
                for (int i = 0; i < solutions.length(); i++) {
                    JSONObject event = solutions.getJSONObject(i);
                    // event data
                    String weatherStation = event.getString("station");
                    String location = event.getString("location");
                    int temperature = event.getInt("temperature");
                    String typeString = event.getString("type");

                    // send standard weather event
                    StandardEvent standardEvent = new StandardEvent(location, temperature);
                    String jsonEvent = new Gson().toJson(standardEvent);
                    ProducerRecord<String, String> standardWeatherEvent = new ProducerRecord<>(STANDARD_WEATHER_TOPIC,
                            weatherStation, jsonEvent);
                    producer.send(standardWeatherEvent);
                    System.out.println("Sent weather event!");

                
                    // send alert event
                    AlertEvent alertEvent = new AlertEvent(location, typeString);
                    String jsonAlertEvent = new Gson().toJson(alertEvent);
                    ProducerRecord<String, String> weatherAlertEvent = new ProducerRecord<>(WEATHER_ALERTS_TOPIC,
                            weatherStation, jsonAlertEvent);
                    producer.send(weatherAlertEvent);
                    System.out.println("Sent weather alert!");
                    
                }           

             } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    System.out.println("Weather events from file completed.");
                    producer.close();
                }
            return;
        }
       
        try {
            while(true) {

                //read here
                Duration d = Duration.ofSeconds(100);
                ConsumerRecords<String, String> records = consumer.poll(d);
                for (ConsumerRecord<String, String> record : records) {
                    // System.out.println(record.value());

                    JSONObject json = new JSONObject(record.value()).getJSONObject("payload");
                    if(json.has("locationName")){
                        String location = json.getString("locationName");
                        if(!locations.contains(location)) {
                            locations.add(location);
                        }
                    }
                    if (json.has("stationName")) {
                        String station = json.getString("stationName");
                        if (!weatherStations.contains(station)) {
                            weatherStations.add(station);
                        }
                    }
                    // System.out.println(locations);
                    // System.out.println(weatherStations);
                }             

                // event data
                String weatherStation = weatherStations.get(randomInt(0, weatherStations.size()));
                String location = locations.get(randomInt(0, locations.size()));
                int temperature = randomInt(MIN_TEMPERATURE, MAX_TEMPERATURE);
    
                // send standard weather event
                StandardEvent event = new StandardEvent(location, temperature);
                String jsonEvent = new Gson().toJson(event);
                ProducerRecord<String, String> standardWeatherEvent = new ProducerRecord<>(STANDARD_WEATHER_TOPIC, weatherStation, jsonEvent);
                producer.send(standardWeatherEvent);
                System.out.println("Sent weather event!");
    
                // event data
                location = locations.get(randomInt(0, locations.size()));
                String type = alertTypes[randomInt(0, alertTypes.length)];

                //send alert event
                AlertEvent alertEvent = new AlertEvent(location, type);
                String jsonAlertEvent = new Gson().toJson(alertEvent);
                ProducerRecord<String, String> weatherAlertEvent = new ProducerRecord<>(WEATHER_ALERTS_TOPIC, weatherStation, jsonAlertEvent);
                producer.send(weatherAlertEvent);
                System.out.println("Sent weather alert!");
    
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            System.out.println("Weather events producer completed.");
            producer.close();
            consumer.close();
        }
    }

    private Properties loadPProducerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "broker1:9092, broker2:9092");
        //Set acknowledgements for producer requests. props.put("acks", "all");
        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);
        //Specify buffer size in config
        props.put("batch.size", 16384);
        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    private Properties loadConsumerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "broker1:9092"); // Set acknowledgements for producer requests. props.put("acks", // "all");
        // If the request fails, the producer can automatically retry,
        props.put("retries", 0);
        // Specify buffer size in config
        props.put("batch.size", 16384);
        // Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        // The buffer.memory controls the total amount of memory available to the
        // producer for buffering.
        props.put("buffer.memory", 33554432);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "WeatherStationConsumer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
        // Serdes.String().getClass());
        // props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
        // Serdes.Long().getClass());
        props.put("max.poll.records", "10000");

        return props;
    }

    // private List loadTopics(){
    //     List<String> list = new ArrayList<String>(11);
    //     for (int i = 1; i <= 11; i++) {
    //         list.add("requirement"+i+"Info");
    //     }
    //     System.out.println(list);
    //     return list;
    //     // return Arrays.asList("requirement1Info", "requirement2Info");
    // }

    private int randomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private float randomFloat(double d, double e) {
        return (float) ((Math.random() * (e - d)) + d);
    }
}