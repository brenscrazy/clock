package eventsstatistics;

import clock.SettableClock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Map;

public class EventsStatisticsImplementationTest {

    private final double DELTA = 0.00001;
    private SettableClock clock;
    private EventsStatistics eventsStatistics;

    @Before
    public void beforeEach() {
        clock = new SettableClock(Instant.now());
        eventsStatistics = new EventsStatisticsImplementation(clock);
    }

    @Test
    public void testSingleRecord() {
        eventsStatistics.incEvent("test-event");
        clock.move(1000);
        Assert.assertEquals(1 / 60.0, eventsStatistics.getEventStatisticByName("test-event"), DELTA);
    }

    @Test
    public void testNoRecord() {
        Assert.assertEquals(0, eventsStatistics.getEventStatisticByName("test-event"), DELTA);
    }

    @Test
    public void testMultipleRecord() {

        final int FIRST_EVENT_AMOUNT = 3;
        final String FIRST_EVENT_NAME = "test-event-1";
        inc(FIRST_EVENT_NAME, 1000, FIRST_EVENT_AMOUNT);

        final int SECOND_EVENT_AMOUNT = 5;
        final String SECOND_EVENT_NAME = "test-event-2";
        inc(SECOND_EVENT_NAME, 1000, SECOND_EVENT_AMOUNT);

        Assert.assertEquals(
                FIRST_EVENT_AMOUNT / 60.0,
                eventsStatistics.getEventStatisticByName(FIRST_EVENT_NAME),
                DELTA);
        Assert.assertEquals(
                SECOND_EVENT_AMOUNT / 60.0,
                eventsStatistics.getEventStatisticByName(SECOND_EVENT_NAME),
                DELTA);

    }

    @Test
    public void testGetAll() {

        final int FIRST_EVENT_AMOUNT = 3;
        final String FIRST_EVENT_NAME = "test-event-1";
        inc(FIRST_EVENT_NAME, 1000, FIRST_EVENT_AMOUNT);

        final int SECOND_EVENT_AMOUNT = 5;
        final String SECOND_EVENT_NAME = "test-event-2";
        inc(SECOND_EVENT_NAME, 1000, SECOND_EVENT_AMOUNT);

        Map<String, Double> stats = eventsStatistics.getAllEventStatistic();
        Assert.assertEquals(
                FIRST_EVENT_AMOUNT / 60.0,
                stats.get(FIRST_EVENT_NAME),
                DELTA);
        Assert.assertEquals(
                SECOND_EVENT_AMOUNT / 60.0,
                stats.get(SECOND_EVENT_NAME),
                DELTA);
    }

    @Test
    public void testDeletion() {

        final int FIRST_EVENT_AMOUNT = 3;
        final String FIRST_EVENT_NAME = "test-event-1";
        inc(FIRST_EVENT_NAME, 1000 * 60 * 25, FIRST_EVENT_AMOUNT);

        Assert.assertEquals(
                (FIRST_EVENT_AMOUNT - 1) / 60.0,
                eventsStatistics.getEventStatisticByName(FIRST_EVENT_NAME),
                DELTA);

    }

    @Test
    public void testPrint() {

        final int FIRST_EVENT_AMOUNT = 296;
        final String FIRST_EVENT_NAME = "upload";
        inc(FIRST_EVENT_NAME, 1000, FIRST_EVENT_AMOUNT);

        final int SECOND_EVENT_AMOUNT = 405;
        final String SECOND_EVENT_NAME = "download";
        inc(SECOND_EVENT_NAME, 1000, SECOND_EVENT_AMOUNT);

        eventsStatistics.printStatistic();

    }

    private void inc(String eventName, long diff, int amount) {
        for (int i = 0; i < amount; i++) {
            eventsStatistics.incEvent(eventName);
            clock.move(diff);
        }
    }

}
