/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package osproj;

/**
 *
 * @author Korisnik
 */
/**
 * Represents a single assignment of a thread to a seat.
 */
public final class Assignment {
    private final int threadId;
    private final int seatId;

    public Assignment(int threadId, int seatId) {
        if (threadId <= 0) throw new IllegalArgumentException("threadId must be > 0");
        if (seatId < 0) throw new IllegalArgumentException("seatId must be >= 0");
        this.threadId = threadId;
        this.seatId = seatId;
    }

    public int getThreadId() {
        return threadId;
    }

    public int getSeatId() {
        return seatId;
    }

    @Override
    public String toString() {
        return "T" + threadId + "→S" + seatId;
    }
}
