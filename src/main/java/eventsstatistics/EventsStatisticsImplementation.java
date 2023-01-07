package eventsstatistics;

import clock.Clock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventsStatisticsImplementation implements EventsStatistics {

    final private Map<String, List<Long>> statistics = new HashMap<>();
    final private Clock clock;

    EventsStatisticsImplementation(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void incEvent(String name) {
        statistics.putIfAbsent(name, new ArrayList<>());
        List<Long> toUpdate = statistics.get(name);
        toUpdate.add(clock.now().toEpochMilli());
    }

    @Override
    public double getEventStatisticByName(String name) {
        if (!statistics.containsKey(name)) {
            return 0;
        }
        List<Long> stats = statistics.get(name);
        List<Long> filteredStats = stats.stream()
                .filter(l -> clock.now().toEpochMilli() - l < 1000 * 60 * 60)
                .toList();
        statistics.put(name, filteredStats);
        return filteredStats.size() / 60.0;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        return statistics.keySet().stream().collect(Collectors.toMap(k -> k, this::getEventStatisticByName));
    }

    @Override
    public void printStatistic() {
        Map<String, Double> stats = getAllEventStatistic();
        int maxLength = stats.keySet().stream().mapToInt(String::length).max().orElse(0);
        for (Map.Entry<String, Double> e : stats.entrySet()) {
            String name = e.getKey();
            double rpm = e.getValue();
            System.out.println(name + " ".repeat(maxLength - name.length()) + " : " + rpm);
        }
    }
}
