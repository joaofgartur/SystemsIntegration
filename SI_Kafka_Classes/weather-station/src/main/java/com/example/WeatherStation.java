package com.example;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.example.Models.AlertEvent;
import com.example.Models.StandardEvent;
import com.google.gson.Gson;

public class WeatherStation {

    private final int MIN_TEMPERATURE = -100, MAX_TEMPERATURE = 100, SLEEP_TIME = 2000;
    private final String STANDARD_WEATHER_TOPIC = "StandardWeatherTopic", WEATHER_ALERTS_TOPIC = "WeatherAlertsTopic";

    public WeatherStation() {
        myMain();
    }

    public static void main(String[] args) throws Exception {
        new WeatherStation();
    }

    private void myMain() {
       Properties producerProperties = loadPProducerProperties();
       Producer<String, String> producer = new KafkaProducer<>(producerProperties);

        String[] weatherStations = {"Coimbra", "Lisboa", "Porto"};
        String[] locations = {"Alta", "Baixa", "Polo II", "Norton"};
        String[] alertTypes = {"red", "alert"};

        try {
            while(true) {
                // event data
                String weatherStation = weatherStations[randomInt(0, weatherStations.length - 1)];
                String location = locations[randomInt(0, locations.length - 1)];
                int temperature = randomInt(MIN_TEMPERATURE, MAX_TEMPERATURE);
    
                // send standard weather event
                StandardEvent event = new StandardEvent(location, temperature);
                String jsonEvent = new Gson().toJson(event);
                ProducerRecord<String, String> standardWeatherEvent = new ProducerRecord<>(STANDARD_WEATHER_TOPIC, weatherStation, jsonEvent);
                producer.send(standardWeatherEvent);
                System.out.println("Sent weather event!");
    
                // event data
                location = locations[randomInt(0, locations.length - 1)];
                String type = alertTypes[randomInt(0, alertTypes.length - 1)];

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
        }
    }

    private Properties loadPProducerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "broker1:9092");
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

    private int randomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private float randomFloat(double d, double e) {
        return (float) ((Math.random() * (e - d)) + d);
    }
}