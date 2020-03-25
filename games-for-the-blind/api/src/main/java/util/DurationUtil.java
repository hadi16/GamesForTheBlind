package util;

import java.time.Duration;

public class DurationUtil {
    public static int toHoursPart(Duration duration) {
        return (int) duration.toHours() % 24;
    }

    public static int toMinutesPart(Duration duration) {
        return (int) duration.toMinutes() % 60;
    }

    public static int toSecondsPart(Duration duration) {
        return (int) duration.getSeconds() % 60;
    }
}
