package gamesforblind;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

public abstract class ProgramAction {
    private final LocalDateTime localDateTime;

    public abstract XmlAdapter getJaxbAdapter();

    public ProgramAction() {
        this.localDateTime = LocalDateTime.now();
    }

    public LocalDateTime getLocalDateTime() {
        return this.localDateTime;
    }
}
