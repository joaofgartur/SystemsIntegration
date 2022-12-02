package com.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class WeatherStation {

    private final int MIN_TEMPERATURE = 32, MAX_TEMPERATURE = 212, SLEEP_TIME = 2000;
    private final String STANDARD_WEATHER_TOPIC = "StandardWeatherTopic", WEATHER_ALERTS_TOPIC = "WeatherAlertsTopic";

    public WeatherStation() {
        myMain();
    }

    public static void main(String[] args) throws Exception {
        new WeatherStation();
    }

    private void myMain() {

        // standard weather events producer
        Properties standardProps = loadProperties(false);
        Producer<String, StandardEvent> standardProducer = new KafkaProducer<>(standardProps);

        // weather alert events producer
        Properties alertProps = loadProperties(true);
        Producer<String, AlertEvent> alertsProducer = new KafkaProducer<>(alertProps);

        String[] weatherStations = {"Coimbra", "Lisboa", "Porto"};
        String[] locations = {"Alta", "Baixa", "Polo II", "Norton"};
        String[] alertTypes = {"red", "alert"};

        String weatherStation = weatherStations[0];

        try {
            while(true) {
                // event data
                String location = locations[randomInt(0, locations.length - 1)];
                int temperature = randomInt(MIN_TEMPERATURE, MAX_TEMPERATURE);
    
                // send standard weather event
                StandardEvent event = new StandardEvent(location, temperature);
                ProducerRecord<String, StandardEvent> standardWeatherEvent = new ProducerRecord<>(STANDARD_WEATHER_TOPIC, weatherStation, event);
                standardProducer.send(standardWeatherEvent);
                System.out.println("Sent weather event!");
    
                // send alert weather event
                float alertProbability = randomFloat(0.0, 1.0);
                if (alertProbability >= 0.5) {
    
                    // event data
                    location = locations[randomInt(0, locations.length - 1)];
                    String type = alertTypes[randomInt(0, alertTypes.length - 1)];
    
                    AlertEvent alertEvent = new AlertEvent(location, type);
                    ProducerRecord<String, AlertEvent> weatherAlertEvent = new ProducerRecord<>(WEATHER_ALERTS_TOPIC, weatherStation, alertEvent);
                    alertsProducer.send(weatherAlertEvent);
                    System.out.println("Sent weather alert!");
                }
    
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            System.out.println("Employee Producer Completed.");
            standardProducer.close();
            alertsProducer.close();
        }
        
    }

    private Properties loadProperties(boolean alertEvent) {
        Properties props = new Properties();

        props.put("bootstrap.servers", "broker1:9092");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        if (alertEvent) {
            props.put("value.serializer", AlertEventSerializer.class);
        } else {
            props.put("value.serializer", StandardEventSerializer.class);
        }

        return props;
    }

    private int randomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private float randomFloat(double d, double e) {
        return (float) ((Math.random() * (e - d)) + d);
    }
}