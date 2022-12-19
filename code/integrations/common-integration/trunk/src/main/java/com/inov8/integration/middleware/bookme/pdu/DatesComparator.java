package com.inov8.integration.middleware.bookme.pdu;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Umar on 10/21/2016.
 */
public class DatesComparator implements Comparator<ShowDates> {
    @Override
    public int compare(ShowDates a, ShowDates b) {

        return a.getBookingDate().compareToIgnoreCase(b.getBookingDate());
    }
}
