package clock;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class SettableClock implements Clock {

    private Instant saved;

    public SettableClock(Instant saved) {
        this.saved = saved;
    }

    @Override
    public Instant now() {
        return saved;
    }

    public void setSaved(Instant saved) {
        this.saved = saved;
    }

    public void move(long diff) {
        saved = saved.plus(diff, ChronoUnit.MILLIS);
    }
}
