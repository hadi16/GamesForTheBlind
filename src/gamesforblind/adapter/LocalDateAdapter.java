package gamesforblind.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<AdaptedLocalDate, LocalDate> {
    @Override
    public LocalDate unmarshal(AdaptedLocalDate adapted) {
        return LocalDate.of(adapted.getYear(), adapted.getMonth(), adapted.getDay());
    }

    @Override
    public AdaptedLocalDate marshal(LocalDate localDate) {
        AdaptedLocalDate adapted = new AdaptedLocalDate();
        adapted.setYear(localDate.getYear());
        adapted.setMonth((short) localDate.getMonthValue());
        adapted.setDay((short) localDate.getDayOfMonth());
        return adapted;
    }
}
