package eventsstatistics;

import java.util.Map;

public interface EventsStatistics {
    void incEvent(String name);
    double getEventStatisticByName(String name);
    Map<String, Double> getAllEventStatistic();
    void printStatistic();
}
