/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
import java.util.Map;

/**
 * Backend logic to summarize stats for GUI display.
 */
public class StatsPanel {

    private final StatsCollector statsCollector;

    public StatsPanel(StatsCollector statsCollector) {
        this.statsCollector = statsCollector;
    }

    /**
     * Returns a formatted summary string
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total Seats Reserved: ").append(statsCollector.getTotalSeatsReserved()).append("\n");
        sb.append("Total Failed Attempts: ").append(statsCollector.getTotalFailures()).append("\n");
        sb.append("Total Duration: ").append(statsCollector.getDurationMs()).append(" ms\n");
        sb.append("Per-thread wins:\n");
        for (Map.Entry<Integer, Integer> entry : statsCollector.getThreadWins().entrySet()) {
            sb.append("  T").append(entry.getKey()).append(" → ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}

