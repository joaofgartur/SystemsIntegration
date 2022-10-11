package org.example;

import java.util.ArrayList;
import java.util.Random;

interface MyFunction {

    int add(int a, int b);
}

public class App {

    public static ArrayList<Integer> randomArrayList(int n) {
        ArrayList<Integer> array = new ArrayList<Integer>(n);

        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for (int i=0; i<n; i++)
        {
            Integer r = rand.nextInt() % 256;
            array.add(r);
        }

        return array;
    }

    public static void main( String[] args ) {

        // Exercise 1
        int n = 10;
        ArrayList<Integer> array = randomArrayList(n);
        System.out.println("Before: " + array);
        array.sort((Integer a, Integer b) -> {
            if (a < b) return -1;
            if (a.equals(b)) return 0;
            return 1;
        });
        System.out.println("After: " + array);

        // Exercise 2
        MyFunction lambda = (a, b) -> {
            return a + b;
        };
        System.out.println(lambda.add(10, 5));
    }
}
