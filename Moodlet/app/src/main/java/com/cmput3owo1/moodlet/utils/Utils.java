package com.cmput3owo1.moodlet.utils;

import java.util.Date;
import java.util.Locale;

public class Utils {

    /**
     * Calculates the time difference between the current date and the specified date. This
     * function is based off of <a href="https://stackoverflow.com/a/10690542">this StackOverflow answer</a>.
     * @param date The date to compare with.
     * @return A formatted string for the time difference.
     */
    public static String getTimeDifference(Date date) {
        // Get the date difference in milliseconds
        long dateDiff = (new Date()).getTime() - date.getTime();

        // Separate into different time units
        long diffSeconds = dateDiff / 1000;
        long diffMinutes = diffSeconds / 60;
        long diffHours = diffMinutes / 60;
        long diffDays = diffHours / 24;
        long diffYears = diffDays / 365;

        if (diffYears >= 1) {
            return String.format(Locale.US, "%d y", (int) diffYears);
        } else if (diffDays >= 1) {
            return String.format(Locale.US, "%d d", (int) diffDays);
        } else if (diffHours >= 1) {
            return String.format(Locale.US, "%d h", (int) diffHours);
        } else if (diffMinutes >= 1) {
            return String.format(Locale.US, "%d m", (int) diffMinutes);
        } else if (diffSeconds >= 1) {
            return String.format(Locale.US, "%d s", (int) diffSeconds);
        } else {
            return "Just now";
        }
    }

}
