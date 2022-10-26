package org.example;

import java.util.Comparator;
import java.util.Date;

public class DateComparator implements Comparator<Date> {

    @Override
    public int compare(Date prod1, Date prod2) {
        return Long.valueOf(prod1.getTime())
                .compareTo(prod2.getTime());
    }
}
