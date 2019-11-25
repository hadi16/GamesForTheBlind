package gamesforblind.adapter;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Class to convert between {@link AdaptedLocalDateTime} and {@link LocalDateTime}.
 */
public class LocalDateTimeAdapter extends XmlAdapter<AdaptedLocalDateTime, LocalDateTime> {
    /**
     * Converts an {@link AdaptedLocalDateTime} to a {@link LocalDateTime}.
     *
     * @param adapted The {@link AdaptedLocalDateTime} to convert.
     * @return A {@link LocalDateTime}, which is equivalent to the passed {@link AdaptedLocalDateTime} object.
     */
    @Override
    public LocalDateTime unmarshal(@NotNull AdaptedLocalDateTime adapted) {
        return LocalDateTime.of(adapted.getDate(), adapted.getTime());
    }

    /**
     * Converts an {@link LocalDateTime} to a {@link AdaptedLocalDateTime}.
     *
     * @param localDateTime The {@link LocalDateTime} to convert.
     * @return An {@link AdaptedLocalDateTime}, which is equivalent to the passed {@link LocalDateTime} object.
     */
    @Override
    public AdaptedLocalDateTime marshal(@NotNull LocalDateTime localDateTime) {
        AdaptedLocalDateTime adapted = new AdaptedLocalDateTime();
        adapted.setDate(
                LocalDate.of(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth())
        );
        adapted.setTime(LocalTime.of(
                localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond(), localDateTime.getNano()
        ));
        return adapted;
    }
}
