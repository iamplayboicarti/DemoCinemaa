package main.java.com.movie.util;


import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
    public static Date getDate(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        return cal.getTime();
    }
}
