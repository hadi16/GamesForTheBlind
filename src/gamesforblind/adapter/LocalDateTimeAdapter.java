package gamesforblind.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateTimeAdapter extends XmlAdapter<AdaptedLocalDateTime, LocalDateTime> {
    @Override
    public LocalDateTime unmarshal(AdaptedLocalDateTime adapted) {
        return LocalDateTime.of(adapted.getDate(), adapted.getTime());
    }

    @Override
    public AdaptedLocalDateTime marshal(LocalDateTime localDateTime) {
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
