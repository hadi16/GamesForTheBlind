package gamesforblind.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;

public class LocalTimeAdapter extends XmlAdapter<AdaptedLocalTime, LocalTime> {
    @Override
    public LocalTime unmarshal(AdaptedLocalTime adapted) {
        return LocalTime.of(adapted.getHour(), adapted.getMinute(), adapted.getSecond(), adapted.getNano());
    }

    @Override
    public AdaptedLocalTime marshal(LocalTime localTime) {
        AdaptedLocalTime adapted = new AdaptedLocalTime();
        adapted.setHour((byte) localTime.getHour());
        adapted.setMinute((byte) localTime.getMinute());
        adapted.setSecond((byte) localTime.getSecond());
        adapted.setNano(localTime.getNano());
        return adapted;
    }
}
