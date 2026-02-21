package top.itsglobally.ban.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeParser {

    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d+)([smhdwy])");

    public static Long parseTime(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        long totalSeconds = 0;
        Matcher matcher = TIME_PATTERN.matcher(input.toLowerCase());

        boolean found = false;
        while (matcher.find()) {
            found = true;
            int amount = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "s":
                    totalSeconds += amount;
                    break;
                case "m":
                    totalSeconds += amount * 60L;
                    break;
                case "h":
                    totalSeconds += amount * 3600L;
                    break;
                case "d":
                    totalSeconds += amount * 86400L;
                    break;
                case "w":
                    totalSeconds += amount * 604800L;
                    break;
                case "y":
                    totalSeconds += amount * 31536000L;
                    break;
            }
        }

        return found ? totalSeconds * 1000 : null;
    }

    public static String formatDuration(long milliseconds) {
        if (milliseconds <= 0) {
            return "Permanent";
        }

        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long years = days / 365;

        if (years > 0) {
            return years + " year" + (years > 1 ? "s" : "");
        } else if (weeks > 0) {
            return weeks + " week" + (weeks > 1 ? "s" : "");
        } else if (days > 0) {
            return days + " day" + (days > 1 ? "s" : "");
        } else if (hours > 0) {
            return hours + " hour" + (hours > 1 ? "s" : "");
        } else if (minutes > 0) {
            return minutes + " minute" + (minutes > 1 ? "s" : "");
        } else {
            return seconds + " second" + (seconds > 1 ? "s" : "");
        }
    }

    public static String formatTimestamp(long timestamp) {
        if (timestamp <= 0) {
            return "Never";
        }

        long now = System.currentTimeMillis();
        long diff = timestamp - now;

        if (diff <= 0) {
            return "Expired";
        }

        return formatDuration(diff);
    }
}