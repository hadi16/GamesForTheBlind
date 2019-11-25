package gamesforblind.adapter;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;

/**
 * Class to convert between {@link AdaptedLocalTime} and {@link LocalTime}.
 */
public class LocalTimeAdapter extends XmlAdapter<AdaptedLocalTime, LocalTime> {
    /**
     * Converts an {@link AdaptedLocalTime} to a {@link LocalTime}.
     *
     * @param adapted The {@link AdaptedLocalTime} to convert.
     * @return A {@link LocalTime}, which is equivalent to the passed {@link AdaptedLocalTime} object.
     */
    @Override
    public LocalTime unmarshal(@NotNull AdaptedLocalTime adapted) {
        return LocalTime.of(adapted.getHour(), adapted.getMinute(), adapted.getSecond(), adapted.getNano());
    }

    /**
     * Converts an {@link LocalTime} to a {@link AdaptedLocalTime}.
     *
     * @param localTime The {@link LocalTime} to convert.
     * @return An {@link AdaptedLocalTime}, which is equivalent to the passed {@link LocalTime} object.
     */
    @Override
    public AdaptedLocalTime marshal(@NotNull LocalTime localTime) {
        AdaptedLocalTime adapted = new AdaptedLocalTime();
        adapted.setHour((byte) localTime.getHour());
        adapted.setMinute((byte) localTime.getMinute());
        adapted.setSecond((byte) localTime.getSecond());
        adapted.setNano(localTime.getNano());
        return adapted;
    }
}
