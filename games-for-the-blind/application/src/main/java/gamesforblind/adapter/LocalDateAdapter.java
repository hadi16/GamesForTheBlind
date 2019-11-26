package gamesforblind.adapter;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * Class to convert between {@link AdaptedLocalDate} and {@link LocalDate}.
 */
public class LocalDateAdapter extends XmlAdapter<AdaptedLocalDate, LocalDate> {
    /**
     * Converts an {@link AdaptedLocalDate} to a {@link LocalDate}.
     *
     * @param adapted The {@link AdaptedLocalDate} to convert.
     * @return A {@link LocalDate}, which is equivalent to the passed {@link AdaptedLocalDate} object.
     */
    @Override
    public LocalDate unmarshal(@NotNull AdaptedLocalDate adapted) {
        return LocalDate.of(adapted.getYear(), adapted.getMonth(), adapted.getDay());
    }

    /**
     * Converts an {@link LocalDate} to a {@link AdaptedLocalDate}.
     *
     * @param localDate The {@link LocalDate} to convert.
     * @return An {@link AdaptedLocalDate}, which is equivalent to the passed {@link LocalDate} object.
     */
    @Override
    public AdaptedLocalDate marshal(@NotNull LocalDate localDate) {
        AdaptedLocalDate adapted = new AdaptedLocalDate();
        adapted.setYear(localDate.getYear());
        adapted.setMonth((short) localDate.getMonthValue());
        adapted.setDay((short) localDate.getDayOfMonth());
        return adapted;
    }
}
