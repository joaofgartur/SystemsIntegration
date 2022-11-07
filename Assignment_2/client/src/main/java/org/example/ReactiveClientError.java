package org.example;

import java.net.HttpURLConnection;
import java.net.URL;


public class ReactiveClientError {
    private final String BASE_URL = "http://localhost:8080";
    private final int MAX_ATTEMPTS = 4, SLEEP_TIME = 2000;

    public ReactiveClientError() {
        myMain();
    }

    private void myMain() {
        long startTime = System.currentTimeMillis();

        try {
            int numAttempts = 0;
            String url = BASE_URL + "/error/";

            while (numAttempts < MAX_ATTEMPTS) {
                HttpURLConnection httpURLConnection=(HttpURLConnection)new URL(url).openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();

                if(responseCode == 200) {
                    System.out.println("[CLIENT] Connection successfully to URL : " + url);
                } else {
                    System.out.println("[CLIENT] Connection failed to URL : " + url + " with response code : " + responseCode);
                }

                Thread.sleep(SLEEP_TIME);

                numAttempts += 1;
            }

            System.out.println("[CLIENT] Max connection attempts reached!");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) {
        ReactiveClientError client = new ReactiveClientError();
    }
}