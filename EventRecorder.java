/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
import java.util.ArrayList;
import java.util.List;

/**
 * Records events for replaying simulation steps.
 */
public class EventRecorder {

    public static class Event {
        public final long timestampMs;
        public final int threadId;
        public final int seatId;
        public final String action; // "CHECK", "DELAY", "ATTEMPT", "SUCCESS", "FAIL"

        public Event(long timestampMs, int threadId, int seatId, String action) {
            this.timestampMs = timestampMs;
            this.threadId = threadId;
            this.seatId = seatId;
            this.action = action;
        }
    }

    private final List<Event> events = new ArrayList<>();

    public void recordEvent(long timestampMs, int threadId, int seatId, String action) {
        events.add(new Event(timestampMs, threadId, seatId, action));
    }

    public List<Event> getEvents() {
        return events;
    }

    public void clear() {
        events.clear();
    }
}
