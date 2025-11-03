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
 * Demonstrates seat map using synchronized methods instead of ReentrantLock.
 */
public class SynchronizedSeatMap implements SeatMap {

    private final int[] seats;

    public SynchronizedSeatMap(int totalSeats) {
        seats = new int[totalSeats];
    }

    @Override
    public synchronized boolean isAvailable(int seatId) {
        return seats[seatId] == 0;
    }

    @Override
    public synchronized boolean tryReserve(int seatId, int threadId) {
        if (seats[seatId] == 0) {
            seats[seatId] = threadId;
            return true;
        }
        return false;
    }

    @Override
    public synchronized int getCurrentHolder(int seatId) {
        return seats[seatId];
    }

    @Override
    public int getTotalSeats() {
        return seats.length;
    }
}
