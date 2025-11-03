/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
import java.util.HashMap;
import java.util.Map;

/**
 * Collects statistics during a simulation.
 */
public class StatsCollector {

    private int totalSeatsReserved = 0;
    private int totalFailures = 0;
    private final Map<Integer, Integer> threadWins = new HashMap<>();
    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.currentTimeMillis();
        totalSeatsReserved = 0;
        totalFailures = 0;
        threadWins.clear();
    }

    public void recordSuccess(int threadId) {
        totalSeatsReserved++;
        threadWins.put(threadId, threadWins.getOrDefault(threadId, 0) + 1);
    }

    public void recordFailure() {
        totalFailures++;
    }

    public void end() {
        endTime = System.currentTimeMillis();
    }

    public long getDurationMs() {
        return endTime - startTime;
    }

    public int getTotalSeatsReserved() {
        return totalSeatsReserved;
    }

    public int getTotalFailures() {
        return totalFailures;
    }

    public Map<Integer, Integer> getThreadWins() {
        return threadWins;
    }
}
